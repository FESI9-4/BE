package com.idol.global.jwt;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class JwtAuthenticationContextImpl implements JwtAuthenticationContext {
    private final String memberId;
    private final String role;

    @Override
    public String getMemberId() {
        return "";
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        String authorityRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return Collections.singletonList(new SimpleGrantedAuthority(authorityRole));
    }
}
