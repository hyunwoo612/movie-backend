package com.cocoh.movie.controller;

import com.cocoh.movie.dto.*;
import com.cocoh.movie.service.RefreshTokenService;
import com.cocoh.movie.service.TokenService;
import com.cocoh.movie.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AddUserRequestDto dto) {
        String sign = userService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sign);
    }

    @PostMapping("/login")
    public ResponseEntity<CreateAccessTokenResponseDto> login(HttpServletResponse response, @RequestBody CreateAccessTokenRequestDto request
    ) {
        CreateAccessTokenResponseDto token = tokenService.getAccessToken(response, request);
        if(token != null)
            return ResponseEntity.ok().body(token);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/login/token")
    public ResponseEntity<CreateAccessTokenResponseDto> tokenLogin(
            HttpServletResponse res, HttpServletRequest req
    ) {
        try {
            CreateAccessTokenResponseDto response = tokenService.refreshAccessToken(res, req);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res) {
        try {
            refreshTokenService.logout(req, res);
            return ResponseEntity.ok().build();
//        } catch (NullPointerException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch (Exception e) {
            log.warn("Logout failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
