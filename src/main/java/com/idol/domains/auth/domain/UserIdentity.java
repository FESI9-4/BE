package com.idol.domains.auth.domain;

import io.jsonwebtoken.Claims;

import java.io.Serializable;
import java.util.UUID;

public record UserIdentity(
        UUID memberId
) implements Serializable {

    public static UserIdentity of(UUID userId) {
        return new UserIdentity(userId);
    }

    public static UserIdentity from(Claims claims) {
        UUID userId = UUID.fromString(claims.getSubject());

        return new UserIdentity(userId);
    }
}
