package com.cocoh.movie.dto;

import com.cocoh.movie.Entity.Movie;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Builder
@Getter
public class MovieListResponse {

    private Long movie_id;
    private String movie_image;
    private String movie_name;
    private String movie_director;
    private Long movie_review_count;
    private Double movie_rating;
    private String movie_genre;
    private LocalDate movie_date;
    private Timestamp created_at;

    public static MovieListResponse from(Movie movie) {
        return MovieListResponse.builder()
                .movie_id(movie.getId())
                .movie_image(movie.getMovie_image())
                .movie_name(movie.getMovieName())
                .movie_director(movie.getDirector().getName())
                .movie_review_count(movie.getMovie_review_count())
                .movie_rating(movie.getMovie_rating())
                .movie_genre(movie.getMovie_genre())
                .movie_date(movie.getMovie_date())
                .created_at(movie.getCreatedAt())
                .build();
    }
}
