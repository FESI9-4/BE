package com.idol.domains.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class AuthenticationResult {
    private final String memberId;
    private final String role;
    private final Map<String, Object> additionalClaims;

    @Builder
    public AuthenticationResult(String memberId, String role, Map<String, Object> additionalClaims) {
        this.memberId = memberId;
        this.role = role;
        this.additionalClaims = additionalClaims;
    }
}
