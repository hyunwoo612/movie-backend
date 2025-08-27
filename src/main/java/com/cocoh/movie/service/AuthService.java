package com.cocoh.movie.service;

import com.cocoh.movie.Entity.RefreshToken;
import com.cocoh.movie.config.jwt.TokenProvider;
import com.cocoh.movie.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    @Transactional(readOnly = true)
    public void logout(String refreshToken) {
        if(refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken).isEmpty()) {
            throw new RuntimeException("RefreshToken not found");
        }
        refreshTokenService.removeRefreshToken(refreshToken);
        SecurityContextHolder.clearContext();
    }
}
