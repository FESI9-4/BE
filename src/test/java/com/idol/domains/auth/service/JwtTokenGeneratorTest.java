package com.idol.domains.auth.service;

import com.idol.domains.auth.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenGeneratorTest {

    @Mock
    private JwtProperties jwtProperties;

    private JwtTokenGenerator tokenGenerator;

    private final String SECRET_KEY = "testSecretKey123456789012345678901234567890";
    private final Long ACCESS_EXPIRATION = 3600000L;
    private final Long REFRESH_EXPIRATION = 604800000L;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        tokenGenerator = new JwtTokenGenerator(jwtProperties);
    }

    @Test
    @DisplayName("액세스 토큰을 생성한다")
    void generateAccessToken_Success() {
        // given
        when(jwtProperties.getSignKey()).thenReturn(secretKey);
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(ACCESS_EXPIRATION);

        String subject = "test@example.com";
        Map<String, Object> claims = Map.of("userId", "12345");

        // when
        String token = tokenGenerator.generateAccessToken(subject, claims);

        // then
        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);

        // 토큰 내용 검증
        Claims parsedClaims = parseToken(token);
        assertThat(parsedClaims.getSubject()).isEqualTo(subject);
        assertThat(parsedClaims.get("userId", String.class)).isEqualTo("12345");
    }

    @Test
    @DisplayName("리프레시 토큰을 생성한다")
    void generateRefreshToken_Success() {
        // given
        when(jwtProperties.getSignKey()).thenReturn(secretKey);
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(REFRESH_EXPIRATION);

        String subject = "test@example.com";

        // when
        String token = tokenGenerator.generateRefreshToken(subject);

        // then
        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);

        Claims parsedClaims = parseToken(token);
        assertThat(parsedClaims.getSubject()).isEqualTo(subject);

        Date expiration = parsedClaims.getExpiration();
        long expirationTime = expiration.getTime() - System.currentTimeMillis();
        assertThat(expirationTime).isLessThanOrEqualTo(REFRESH_EXPIRATION);
        assertThat(expirationTime).isGreaterThan(REFRESH_EXPIRATION - 5000);
    }

    @Test
    @DisplayName("빈 클레임으로 액세스 토큰을 생성한다")
    void generateAccessToken_EmptyClaims_Success() {
        // given
        when(jwtProperties.getSignKey()).thenReturn(secretKey);
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(ACCESS_EXPIRATION);

        String subject = "test@example.com";
        Map<String, Object> emptyClaims = Map.of();

        // when
        String token = tokenGenerator.generateAccessToken(subject, emptyClaims);

        // then
        assertThat(token).isNotNull();
        Claims parsedClaims = parseToken(token);
        assertThat(parsedClaims.getSubject()).isEqualTo(subject);
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
