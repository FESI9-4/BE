package com.idol.domains.auth.service;

import com.idol.domains.auth.domain.RefreshToken;
import com.idol.domains.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    public void saveRefreshToken(String memberId, String token) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByMemberId(memberId);

        if (existingToken.isPresent()) {
            RefreshToken existing = existingToken.get();

            refreshTokenRepository.delete(existing);

            redisTemplate.delete("refreshToken:" + existing.getId());
            redisTemplate.delete("refreshToken:memberId:" + memberId);
            redisTemplate.delete("refreshToken:token:" + existing.getToken());
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .memberId(memberId)
                .token(token)
                .expiration(refreshTokenExpiration / 1000)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token).isPresent();
    }

    public String getRefreshTokenByMemberId(String memberId) {
        return refreshTokenRepository.findByMemberId(memberId)
                .map(RefreshToken::getToken)
                .orElse(null);
    }

    public void deleteRefreshToken(String memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }

    public String getMemberIdByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(RefreshToken::getMemberId)
                .orElse(null);
    }

    private void deleteAllTokensForMember(String memberId) {
        refreshTokenRepository.findByMemberId(memberId).ifPresent(existing -> {
            refreshTokenRepository.delete(existing);

            Set<String> keysToDelete = Set.of(
                    "refreshToken:" + existing.getId(),
                    "refreshToken:memberId:" + memberId,
                    "refreshToken:token:" + existing.getToken()
            );
            redisTemplate.delete(keysToDelete);

            redisTemplate.opsForSet().remove("refreshToken", existing.getId());

            log.debug("Deleted all tokens for member: {}", memberId);
        });
    }

}
