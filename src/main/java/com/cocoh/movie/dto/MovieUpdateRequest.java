package com.cocoh.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class MovieUpdateRequest {
    private String movie_name;
    private Time movie_time;
    private String movie_director;
    private Integer movie_review_count;
    private String movie_cast_list;
}
