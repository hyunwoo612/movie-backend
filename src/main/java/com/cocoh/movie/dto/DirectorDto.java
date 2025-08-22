package com.cocoh.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DirectorDto {
    private String name;
    private LocalDate birthDay;
}
