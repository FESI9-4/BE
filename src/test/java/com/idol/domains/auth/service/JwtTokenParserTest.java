package com.idol.domains.auth.service;

import com.idol.domains.auth.config.JwtProperties;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenParserTest {

    @Mock
    private JwtProperties jwtProperties;

    private JwtTokenParser tokenParser;
    private SecretKey secretKey;

    private final String SECRET_KEY = "testSecretKey123456789012345678901234567890";

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        when(jwtProperties.getSignKey()).thenReturn(secretKey);

        tokenParser = new JwtTokenParser(jwtProperties);
    }

    @Test
    @DisplayName("토큰에서 subject를 추출한다")
    void extractSubject_Success() {
        // given
        String subject = "test@example.com";
        String token = createTestToken(subject, Map.of());

        // when
        String extractedSubject = tokenParser.extractSubject(token);

        // then
        assertThat(extractedSubject).isEqualTo(subject);
    }

    @Test
    @DisplayName("토큰에서 만료시간을 추출한다")
    void extractExpiration_Success() {
        // given
        String token = createTestToken("test", Map.of());

        // when
        Date expiration = tokenParser.extractExpiration(token);

        // then
        assertThat(expiration).isAfter(new Date());
    }

    @Test
    @DisplayName("잘못된 토큰 파싱 시 예외가 발생한다")
    void extractAllClaims_InvalidToken_ThrowsException() {
        // given
        String invalidToken = "invalid.token.format";

        // when & then
        assertThatThrownBy(() -> tokenParser.extractAllClaims(invalidToken))
                .isInstanceOf(Exception.class);
    }

    private String createTestToken(String subject, Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + 3600000))
                .signWith(secretKey)
                .compact();
    }
}
