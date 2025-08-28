package com.cocoh.movie.service;

import com.cocoh.movie.Entity.RefreshToken;
import com.cocoh.movie.config.jwt.TokenProvider;
import com.cocoh.movie.repository.RefreshTokenRepository;
import com.cocoh.movie.utils.CookieUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import com.cocoh.movie.utils.CookieUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final TokenProvider tokenProvider;


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

    @Transactional
    public void removeRefreshTokenByUsername(String username) {
        Boolean deleted = redisTemplate.delete(username);
        if (deleted == null || !deleted) {
            log.warn("Failed to delete refresh token from Redis for user: {}", username);
        } else {
            log.info("Successfully deleted refresh token from Redis for user: {}", username);
        }
    }


    @Transactional(readOnly = true)
    public RefreshToken findByUsername(String username) {
        return refreshTokenRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        try {
            Cookie refreshTokenCookie = CookieUtil.getCookie(req, "refresh-token");
            log.warn("refresh-token cookie: {}", refreshTokenCookie.getValue());

            Cookie accessTokenCookie = CookieUtil.getCookie(req, "access-token");
            if (refreshTokenCookie == null) {
                log.warn("Refresh token cookie not found during logout");
                SecurityContextHolder.clearContext();
                throw new Exception("Refresh token cookie not found during logout");
            }

            if (accessTokenCookie != null) {
                accessTokenCookie.setValue(null);
                accessTokenCookie.setMaxAge(0);
                res.addCookie(accessTokenCookie);
            }

            String refreshToken = refreshTokenCookie.getValue();
            log.info("Refreshing access token: {}", refreshToken);

            Claims claims = tokenProvider.getClaims(refreshToken);
            String username = claims.getSubject();
            log.info("Logging out user: {}", username);

            refreshTokenCookie.setValue(null);
            refreshTokenCookie.setMaxAge(0);
            res.addCookie(refreshTokenCookie);

            accessTokenCookie.setValue(null);
            accessTokenCookie.setMaxAge(0);
            res.addCookie(accessTokenCookie);

            removeRefreshToken(refreshToken);
            removeRefreshTokenByUsername(username);
            SecurityContextHolder.clearContext();
            log.info("Logout successful for user: {}", username);

        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        }
        catch (Exception e) {
            log.error("Error during logout process", e);
            throw new RuntimeException(e);
        }
    }



}
