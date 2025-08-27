package com.cocoh.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenByRefreshTokenDto {
    private String refreshToken;
}
