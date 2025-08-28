package com.cocoh.movie.service;

import com.cocoh.movie.Entity.RefreshToken;
import com.cocoh.movie.Entity.User;
import com.cocoh.movie.config.JwtProperties;
import com.cocoh.movie.config.jwt.TokenProvider;
import com.cocoh.movie.dto.CreateAccessTokenRequestDto;
import com.cocoh.movie.dto.CreateAccessTokenResponseDto;
import com.cocoh.movie.repository.RefreshTokenRepository;
import com.cocoh.movie.utils.CookieUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;

    public CreateAccessTokenResponseDto getAccessToken(HttpServletResponse response, CreateAccessTokenRequestDto request) {
        User user = userService.getUserByUsername(request.getUsername());
        if(user != null) {
            if(passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return createAccessToken(response, user, null);
            }
        }
        return null;
    }

    private CreateAccessTokenResponseDto createAccessToken(HttpServletResponse response, User user, String refreshToken) {
        Duration tokenDuration = Duration.ofMinutes(jwtProperties.getDuration());
        Duration refreshDuration = Duration.ofMinutes(jwtProperties.getRefreshDuration());

        // refreshToken 검색
        RefreshToken savedRefreshToken = refreshTokenService.findByUsername(user.getUsername());

        if(savedRefreshToken != null && refreshToken != null) {
            // 전달 받은 리프레시 토큰이 사용자에게 저장된 토큰과 다르다면
            if(!savedRefreshToken.getRefreshToken().equals(refreshToken))
                return new CreateAccessTokenResponseDto("Invalid token.", null);
        }

        String accessToken = tokenProvider.generateToken(user, tokenDuration, true);
        String newRefreshToken = tokenProvider.generateToken(user, refreshDuration, false);

//        int accessttl = 60 * 60;
        int ttl = 1000 * 60 * 60 * 24;

        refreshTokenService.saveRefreshToken(newRefreshToken, user.getUsername());

//        CookieUtil.addCookie(response, "access-token", accessToken, accessttl, false);
        CookieUtil.addCookie(response, "refresh-token", newRefreshToken, ttl, true);

        return new CreateAccessTokenResponseDto("LS", accessToken);
    }

    public CreateAccessTokenResponseDto refreshAccessToken(HttpServletResponse response, HttpServletRequest request) {
        try {
            Cookie refreshCookie = CookieUtil.getCookie(request, "refresh-token");

            log.info("refresh-token cookie: {}", refreshCookie.getValue());

            if (refreshCookie == null) {
                log.error("refresh-token cookie not found.");
                throw new RuntimeException("Refresh token not found");
            }

            String token = CookieUtil.getCookie(request, "refresh-token").getValue();

            log.info("Refreshing access token: {}", token);

            Claims claims = tokenProvider.getClaims(token);
            String type = claims.get("type").toString();
            if (type == null || !type.equals("R")) {
                throw new Exception("Invalid token");
            }

            User user = userService.getUserByUsername(claims.getSubject());
            return createAccessToken(response, user, token);
        } catch (ExpiredJwtException e) {
            return new CreateAccessTokenResponseDto("만료된 토큰", null);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
