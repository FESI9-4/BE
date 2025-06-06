package com.idol.domains.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService jwtTokenService;

    private final String SECRET_KEY = "mySecretKey123456789012345678901234567890";
    private final Long EXPIRATION_TIME = 3600000L;
    private final Long REFRESH_EXPIRATION_TIME = 604800000L;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenService, "expirationTime", EXPIRATION_TIME);
        ReflectionTestUtils.setField(jwtTokenService, "refreshExpirationTime", REFRESH_EXPIRATION_TIME);
    }

    @Test
    @DisplayName("액세스 토큰을 성공적으로 생성한다")
    void generateAccessToken_Success() {
        // given
        String username = "testuser@example.com";

        // when
        String token = jwtTokenService.generateAccessToken(username);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("리프레시 토큰을 성공적으로 생성한다")
    void generateRefreshToken_Success() {
        // given
        String username = "testuser@example.com";

        // when
        String token = jwtTokenService.generateRefreshToken(username);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();

        Date expiration = jwtTokenService.extractExpiration(token);
        long expirationTime = expiration.getTime() - System.currentTimeMillis();
        assertThat(expirationTime).isLessThanOrEqualTo(REFRESH_EXPIRATION_TIME);
        assertThat(expirationTime).isGreaterThan(REFRESH_EXPIRATION_TIME - 5000);
    }

    @Test
    @DisplayName("클레임과 함께 토큰을 생성한다")
    void generateTokenWithClaims_Success() {
        // given
        String username = "testuser@example.com";
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", "12345");
        claims.put("role", "USER");

        // when
        String token = jwtTokenService.generateTokenWithClaims(username, claims);

        // then
        assertThat(token).isNotNull();

        Claims extractedClaims = extractAllClaims(token);
        assertThat(extractedClaims.get("userId", String.class)).isEqualTo("12345");
        assertThat(extractedClaims.get("role", String.class)).isEqualTo("USER");
        assertThat(extractedClaims.getSubject()).isEqualTo(username);
    }

    @Test
    @DisplayName("토큰에서 사용자명을 추출한다")
    void extractUsername_Success() {
        // given
        String username = "testuser@example.com";
        String token = jwtTokenService.generateAccessToken(username);

        // when
        String extractedUsername = jwtTokenService.extractUsername(token);

        // then
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    @DisplayName("토큰에서 만료 시간을 추출한다")
    void extractExpiration_Success() {
        // given
        String username = "testuser@example.com";
        long beforeCreation = System.currentTimeMillis();
        String token = jwtTokenService.generateAccessToken(username);
        long afterCreation = System.currentTimeMillis();

        // when
        Date expiration = jwtTokenService.extractExpiration(token);

        // then
        long expectedMinExpiration = beforeCreation + EXPIRATION_TIME;
        long expectedMaxExpiration = afterCreation + EXPIRATION_TIME;

        assertThat(expiration.getTime())
                .isBetween(expectedMinExpiration, expectedMaxExpiration);
    }

    @Test
    @DisplayName("토큰에서 특정 클레임을 추출한다")
    void extractClaim_Success() {
        // given
        String username = "testuser@example.com";
        Map<String, Object> claims = new HashMap<>();
        claims.put("customClaim", "customValue");
        String token = jwtTokenService.generateTokenWithClaims(username, claims);

        // when
        String customClaim = jwtTokenService.extractClaim(token,
                extractedClaims -> extractedClaims.get("customClaim", String.class));

        // then
        assertThat(customClaim).isEqualTo("customValue");
    }

    @Test
    @DisplayName("유효한 토큰을 검증한다")
    void validateToken_ValidToken_Success() {
        // given
        String username = "testuser@example.com";
        String token = jwtTokenService.generateAccessToken(username);

        // when
        Boolean isValid = jwtTokenService.validateToken(token, username);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("다른 사용자명으로 토큰 검증 시 실패한다")
    void validateToken_WrongUsername_Fail() {
        // given
        String username = "testuser@example.com";
        String wrongUsername = "wronguser@example.com";
        String token = jwtTokenService.generateAccessToken(username);

        // when
        Boolean isValid = jwtTokenService.validateToken(token, wrongUsername);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("유효한 토큰인지 확인한다")
    void isTokenValid_ValidToken_Success() {
        // given
        String username = "testuser@example.com";
        String token = jwtTokenService.generateAccessToken(username);

        // when
        Boolean isValid = jwtTokenService.isTokenValid(token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("잘못된 형식의 토큰은 유효하지 않다")
    void isTokenValid_InvalidToken_Fail() {
        // given
        String invalidToken = "invalid.token.format";

        // when
        Boolean isValid = jwtTokenService.isTokenValid(invalidToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("만료된 토큰은 유효하지 않다")
    void isTokenValid_ExpiredToken_Fail() {
        // given
        String username = "testuser@example.com";

        ReflectionTestUtils.setField(jwtTokenService, "expirationTime", 1L);
        String token = jwtTokenService.generateAccessToken(username);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // when
        Boolean isValid = jwtTokenService.isTokenValid(token);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("null 토큰은 유효하지 않다")
    void isTokenValid_NullToken_Fail() {
        // when
        Boolean isValid = jwtTokenService.isTokenValid(null);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("빈 토큰은 유효하지 않다")
    void isTokenValid_EmptyToken_Fail() {
        // when
        Boolean isValid = jwtTokenService.isTokenValid("");

        // then
        assertThat(isValid).isFalse();
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

