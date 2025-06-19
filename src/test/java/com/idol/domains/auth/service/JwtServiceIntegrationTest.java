package com.idol.domains.auth.service;

import com.idol.domains.auth.repository.RefreshTokenRepository;
import com.idol.global.config.TestcontainersRedisConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestcontainersRedisConfig.class)
class JwtServiceIntegrationTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("토큰 생성과 검증의 전체 플로우가 정상 작동한다")
    void tokenLifecycle_Success() {
        // given
        String memberId = "12345";
        Map<String, Object> claims = new HashMap<>();

        // when
        String accessToken = jwtService.generateAccessToken(memberId, claims);
        String refreshToken = jwtService.generateRefreshToken(memberId);

        // then
        assertThat(jwtService.isTokenValid(accessToken)).isTrue();
        assertThat(jwtService.isTokenValid(refreshToken)).isTrue();

        assertThat(jwtService.extractMemberId(accessToken)).isEqualTo(memberId);
        assertThat(jwtService.extractMemberId(refreshToken)).isEqualTo(memberId);

        assertThat(jwtService.validateRefreshToken(refreshToken)).isTrue();
    }

    @Test
    @DisplayName("잘못된 멤버ID로 토큰 검증 시 실패한다")
    void validateToken_WrongMemberId_Fail() {
        // given
        String memberId = "12345";
        String wrongMemberId = "67890";
        String token = jwtService.generateAccessToken(memberId);

        // when
        boolean isValid = jwtService.validateToken(token, wrongMemberId);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("빈 claims로 액세스 토큰을 생성할 수 있다")
    void generateAccessToken_EmptyClaims_Success() {
        // given
        String memberId = "12345";

        // when
        String token = jwtService.generateAccessToken(memberId);

        // then
        assertThat(jwtService.isTokenValid(token)).isTrue();
        assertThat(jwtService.extractMemberId(token)).isEqualTo(memberId);
    }

    @Test
    @DisplayName("존재하지 않는 리프레시 토큰 검증 시 실패한다")
    void validateRefreshToken_NotExists_Fail() {
        // given
        String fakeToken = "fake.refresh.token";

        // when
        boolean isValid = jwtService.validateRefreshToken(fakeToken);

        // then
        assertThat(isValid).isFalse();
    }
}
