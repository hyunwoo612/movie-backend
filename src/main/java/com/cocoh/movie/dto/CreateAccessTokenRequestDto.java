package com.cocoh.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenRequestDto {
    private String username;
    private String password;
}
