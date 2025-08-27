package com.cocoh.movie.controller;

import com.cocoh.movie.Entity.User;
import com.cocoh.movie.dto.*;
import com.cocoh.movie.service.RefreshTokenService;
import com.cocoh.movie.service.TokenService;
import com.cocoh.movie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
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
    public ResponseEntity<CreateAccessTokenResponseDto> login(
            @RequestBody CreateAccessTokenRequestDto request
    ) {
        CreateAccessTokenResponseDto token = tokenService.getAccessToken(request);
        if(token != null)
            return ResponseEntity.ok().body(token);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/login/token")
    public ResponseEntity<CreateAccessTokenResponseDto> tokenLogin(
            @RequestBody CreateAccessTokenByRefreshTokenDto request
    ) {
        CreateAccessTokenResponseDto response = tokenService.refreshAccessToken(request);
        if(response != null)
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequestDto dto) {
        refreshTokenService.logout(dto.getRefreshToken());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
