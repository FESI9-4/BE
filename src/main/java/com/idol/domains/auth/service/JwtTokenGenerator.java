package com.idol.domains.auth.service;

import com.idol.domains.auth.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, jwtProperties.getAccessTokenExpiration());
    }

    public String generateRefreshToken(String subject) {
        return generateToken(subject, Map.of(), jwtProperties.getRefreshTokenExpiration());
    }

    private String generateToken(String subject, Map<String, Object> claims, Long expiration) {
        long currentTimeMillis = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(currentTimeMillis + expiration))
                .signWith(jwtProperties.getSignKey())
                .compact();
    }
}
