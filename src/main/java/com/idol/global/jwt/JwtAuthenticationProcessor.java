package com.idol.global.jwt;

import com.idol.domains.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProcessor {

    private final JwtService jwtService;

    public JwtAuthenticationContext extractAuthenticationContext(String token) {
        String memberId = jwtService.extractMemberId(token);
        String role = jwtService.extractClaim(token, "role", String.class);

        return JwtAuthenticationContextImpl.builder()
                .memberId(memberId)
                .role(role != null ? role : "USER")
                .build();
    }

    public boolean isValidToken(String token) {
        return jwtService.isTokenValid(token);
    }
}
