package com.cocoh.movie.dto;

import com.cocoh.movie.Entity.Movie;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class MovieListResponse {

    private Long id;
    private String movie_name;
    private String movie_director;
    private Integer movie_review_count;
    private Double movie_rating;
    private Timestamp created_at;

    public static MovieListResponse from(Movie movie) {
        return MovieListResponse.builder()
                .id(movie.getId())
                .movie_name(movie.getMovie_name())
                .movie_director(movie.getMovie_director())
                .movie_review_count(movie.getMovie_review_count())
                .movie_rating(movie.getMovie_rating())
                .created_at(movie.getCreatedAt())
                .build();
    }
}
