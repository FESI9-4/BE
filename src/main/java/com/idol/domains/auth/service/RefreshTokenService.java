package com.idol.domains.auth.service;

import com.idol.domains.auth.domain.RefreshToken;
import com.idol.domains.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void saveRefreshToken(Long memberId, String token) {
        String memberIdStr = String.valueOf(memberId);

        deleteAllTokensForMember(memberIdStr);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .memberId(memberIdStr)
                .token(token)
                .expiration(refreshTokenExpiration / 1000)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    public void saveRefreshToken(String memberId, String token) {
        deleteAllTokensForMember(memberId);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .memberId(memberId)
                .token(token)
                .expiration(refreshTokenExpiration / 1000)
                .build();

        refreshTokenRepository.save(refreshToken);
        log.debug("Saved refresh token for member: {}", memberId);
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token).isPresent();
    }

    public String getRefreshTokenByMemberId(Long memberId) {
        return getRefreshTokenByMemberId(String.valueOf(memberId));
    }

    public String getRefreshTokenByMemberId(String memberId) {
        return refreshTokenRepository.findByMemberId(memberId)
                .map(RefreshToken::getToken)
                .orElse(null);
    }

    public void deleteRefreshToken(Long memberId) {
        deleteRefreshToken(String.valueOf(memberId));
    }

    public void deleteRefreshToken(String memberId) {
        deleteAllTokensForMember(memberId);
    }

    public Long getMemberIdByToken(String token) {
        String memberIdStr = getMemberIdStringByToken(token);
        if (memberIdStr != null) {
            try {
                return Long.parseLong(memberIdStr);
            } catch (NumberFormatException e) {
                log.error("Invalid member ID format: {}", memberIdStr);
                return null;
            }
        }
        return null;
    }

    public String getMemberIdStringByToken(String token) {
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

    public boolean isValidMemberId(String memberId) {
        if (memberId == null || memberId.isEmpty()) {
            return false;
        }

        try {
            Long id = Long.parseLong(memberId);
            return id > 0;
        } catch (NumberFormatException e) {
                return false;
        }
    }
}