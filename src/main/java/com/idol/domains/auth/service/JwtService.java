package com.idol.domains.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenGenerator tokenGenerator;
    private final JwtTokenParser tokenParser;
    private final JwtTokenValidator tokenValidator;
    private final RefreshTokenService refreshTokenService;

    public String generateAccessToken(String username) {
        return tokenGenerator.generateAccessToken(username, new HashMap<>());
    }

    public String generateAccessToken(String memberId, Map<String, Object> claims) {
        return tokenGenerator.generateAccessToken(memberId, claims);
    }

    public String generateRefreshToken(String memberId) {
        String refreshToken = tokenGenerator.generateRefreshToken(memberId);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);
        return refreshToken;
    }

    public String extractMemberId(String token) {
        return tokenParser.extractSubject(token);
    }

    public <T> T extractClaim(String token, String claimName, Class<T> claimType) {
        return tokenParser.extractClaim(token, claims -> claims.get(claimName, claimType));
    }

    public boolean isTokenValid(String token) {
        return tokenValidator.isTokenValid(token);
    }

    public boolean validateToken(String token, String username) {
        return tokenValidator.validateToken(token, username);
    }

    public boolean validateRefreshToken(String token) {
        if (!isTokenValid(token)) {
            return false;
        }

        return refreshTokenService.validateRefreshToken(token);
    }

    public void revokeRefreshToken(String memberId) {
        refreshTokenService.deleteRefreshToken(memberId);
    }

}
