package com.idol.global.jwt;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface JwtAuthenticationContext {
    String getMemberId();
    String getRole();
    List<SimpleGrantedAuthority> getAuthorities();
}
