package com.cocoh.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateAccessTokenResponseDto {
    private String result;
    private String token;
    private String refreshToken;
}
