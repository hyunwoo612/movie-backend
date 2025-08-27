package com.cocoh.movie.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60*60*24)
public class RefreshToken implements Serializable {

    @Id
    @Indexed
    private String refreshToken;

    private String username;

    @TimeToLive
    private Long ttl;

    @Builder
    public RefreshToken(String refreshToken, String username) {
        this.refreshToken = refreshToken;
        this.username = username;
        this.ttl = 1000L * 60 * 60 * 24;
    }
}
