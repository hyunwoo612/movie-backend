package com.cocoh.movie.dto;

import com.cocoh.movie.Entity.Director;
import com.cocoh.movie.Entity.Movie;
import com.cocoh.movie.Entity.Review;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class MovieResponseDto {

    Long movie_id;
    String movie_name;
    LocalDate movie_date;
    LocalTime movie_time;
    String movie_director;
    Long director_id;
    Long movie_review_count;
    String movie_cast_list;
    double movie_rating;
    String movie_genre;
    String movie_description;
    String movie_image;
    Timestamp created_at;
    Timestamp updated_at;
    List<Review> reviews;

    public MovieResponseDto(Movie movie) {
        this.movie_id = movie.getId();
        this.movie_name = movie.getMovieName();
        this.movie_date = movie.getMovie_date();
        this.movie_time = movie.getMovie_time();
        this.movie_director = movie.getDirector().getName();
        this.director_id = movie.getDirector().getId();
        this.movie_review_count = movie.getMovie_review_count();
        this.movie_cast_list = movie.getMovie_cast_list();
        this.movie_rating = movie.getMovie_rating();
        this.movie_genre = movie.getMovie_genre();
        this.movie_description = movie.getMovie_description();
        this.movie_image = movie.getMovie_image();
        this.created_at = movie.getCreatedAt();
        this.updated_at = movie.getUpdatedAt();
        this.reviews = movie.getReviews();
    }
}
