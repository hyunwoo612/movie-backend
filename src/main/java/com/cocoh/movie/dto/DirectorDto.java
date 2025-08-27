package com.cocoh.movie.dto;

import com.cocoh.movie.Entity.Director;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class DirectorDto {
    private Long id;
    private String name;
    private LocalDate birthDay;
    private String director_images;

    public static DirectorDto from(Director director) {
        return DirectorDto.builder()
                .id(director.getId())
                .name(director.getName())
                .birthDay(director.getBirthDay())
                .director_images(director.getDirector_image())
                .build();
    }


}
