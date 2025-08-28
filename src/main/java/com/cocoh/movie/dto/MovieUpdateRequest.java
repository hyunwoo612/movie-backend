package com.cocoh.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MovieUpdateRequest {
    private String movie_name;
    private LocalTime movie_time;
    private LocalDate movie_date;
    private String movie_director;
    private String movie_cast_list;
    private String movie_description;
    private String movie_genre;
    private String movie_image;
    private Long director_id;
}
