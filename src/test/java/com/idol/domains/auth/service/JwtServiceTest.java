package com.idol.domains.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtTokenGenerator tokenGenerator;

    @Mock
    private JwtTokenParser tokenParser;

    @Mock
    private JwtTokenValidator tokenValidator;

    @Mock
    private RefreshTokenService refreshTokenService;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(tokenGenerator, tokenParser, tokenValidator, refreshTokenService);
    }

    @Test
    @DisplayName("액세스 토큰을 성공적으로 생성한다 - claims 없이")
    void generateAccessToken_WithoutClaims_Success() {
        // given
        String username = "test@example.com";
        String expectedToken = "test.access.token";
        when(tokenGenerator.generateAccessToken(eq(username), any(Map.class)))
                .thenReturn(expectedToken);

        // when
        String token = jwtService.generateAccessToken(username);

        // then
        assertThat(token).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("액세스 토큰을 성공적으로 생성한다 - claims 포함")
    void generateAccessToken_WithClaims_Success() {
        // given
        String username = "test@example.com";
        Map<String, Object> claims = Map.of("userId", "12345", "role", "USER");
        String expectedToken = "test.access.token.with.claims";
        when(tokenGenerator.generateAccessToken(username, claims))
                .thenReturn(expectedToken);

        // when
        String token = jwtService.generateAccessToken(username, claims);

        // then
        assertThat(token).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("리프레시 토큰을 성공적으로 생성한다")
    void generateRefreshToken_Success() {
        // given
        String username = "test@example.com";
        String expectedToken = "test.refresh.token";
        when(tokenGenerator.generateRefreshToken(username))
                .thenReturn(expectedToken);

        // when
        String token = jwtService.generateRefreshToken(username);

        // then
        assertThat(token).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("토큰에서 회원 데이터 id값 을 추출한다")
    void extractMemberId_Success() {
        // given
        String token = "test.token";
        String expectedMemberId = "123456";
        when(tokenParser.extractSubject(token))
                .thenReturn(expectedMemberId);

        // when
        String memberId = jwtService.extractMemberId(token);

        // then
        assertThat(memberId).isEqualTo(expectedMemberId);
    }

    @Test
    @DisplayName("토큰에서 특정 클레임을 추출한다")
    void extractClaim_Success() {
        // given
        String token = "test.token";
        String claimName = "userId";
        String expectedValue = "12345";

        when(tokenParser.extractClaim(eq(token), any()))
                .thenReturn(expectedValue);

        // when
        String userId = jwtService.extractClaim(token, claimName, String.class);

        // then
        assertThat(userId).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("유효한 토큰을 검증한다")
    void isTokenValid_Success() {
        // given
        String token = "valid.token";
        when(tokenValidator.isTokenValid(token))
                .thenReturn(true);

        // when
        boolean isValid = jwtService.isTokenValid(token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("토큰과 사용자명을 함께 검증한다")
    void validateToken_Success() {
        // given
        String token = "valid.token";
        String username = "test@example.com";
        when(tokenValidator.validateToken(token, username))
                .thenReturn(true);

        // when
        boolean isValid = jwtService.validateToken(token, username);

        // then
        assertThat(isValid).isTrue();
    }
}
