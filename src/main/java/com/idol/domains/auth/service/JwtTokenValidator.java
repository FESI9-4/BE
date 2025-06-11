package com.idol.domains.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtTokenParser tokenParser;

    public boolean isTokenValid(String token) {
        try {
            tokenParser.extractAllClaims(token);
            return isTokenExpired(token);
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token, String expectedSubject) {
        try {
            String subject = tokenParser.extractSubject(token);
            return subject.equals(expectedSubject) && isTokenExpired(token);
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = tokenParser.extractExpiration(token);
        return !expiration.before(new Date());
    }
}
