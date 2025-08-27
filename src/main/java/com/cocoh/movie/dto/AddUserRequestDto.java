package com.cocoh.movie.dto;

import com.cocoh.movie.common.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequestDto {
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;
    @NotBlank
    @Size(min = 8, max = 16)
    private String password;
    private Role role;
}
