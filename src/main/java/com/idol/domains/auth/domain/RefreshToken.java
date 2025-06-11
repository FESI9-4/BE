package com.idol.domains.auth.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String id;

    @Indexed
    private String memberId;

    @Indexed
    private String token;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;
}
