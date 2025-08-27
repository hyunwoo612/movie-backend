package com.cocoh.movie.service;

import com.cocoh.movie.Entity.RefreshToken;
import com.cocoh.movie.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void saveRefreshToken(String refreshToken, String username) {
        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .username(username)
                .build();

        redisTemplate.opsForValue().set(
                username,
                refreshToken,
                token.getTtl(),
                TimeUnit.SECONDS
        );
    }

    @Transactional
    public void removeRefreshToken(String refreshToken) {
        refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Transactional(readOnly = true)
    public RefreshToken findByUsername(String username) {
        return refreshTokenRepository.findByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public void logout(String refreshToken) {
        if(refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken).isEmpty()) {
            throw new RuntimeException("RefreshToken not found");
        }
        removeRefreshToken(refreshToken);
        SecurityContextHolder.clearContext();
    }

}
