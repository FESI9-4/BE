package com.idol.domains.auth.domain;

import io.jsonwebtoken.Claims;

import java.io.Serializable;

public record UserIdentity(
        Long memberId
) implements Serializable {

    public static UserIdentity of(Long userId) {
        return new UserIdentity(userId);
    }

    public static UserIdentity from(Claims claims) {
        Long userId = Long.parseLong(claims.getSubject());

        return new UserIdentity(userId);
    }

    public String getMemberIdAsString() {
        return String.valueOf(memberId);
    }
}
