package com.idol.domains.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class JwtTokenServiceIntegrationTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    @DisplayName("토큰 생성과 검증의 전체 플로우가 정상 작동한다")
    void tokenLifecycle_Success() {
        // given
        String username = "integrationtest@example.com";
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", "67890");

        // when
        String accessToken = jwtTokenService.generateTokenWithClaims(username, claims);
        String refreshToken = jwtTokenService.generateRefreshToken(username);

        // then
        assertThat(jwtTokenService.isTokenValid(accessToken)).isTrue();
        assertThat(jwtTokenService.isTokenValid(refreshToken)).isTrue();

        assertThat(jwtTokenService.extractUsername(accessToken)).isEqualTo(username);
        assertThat(jwtTokenService.extractUsername(refreshToken)).isEqualTo(username);

        String userId = jwtTokenService.extractClaim(accessToken,
                c -> c.get("userId", String.class));

        assertThat(userId).isEqualTo("67890");
    }
}
