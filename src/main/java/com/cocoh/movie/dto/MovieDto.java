package com.cocoh.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDto {
    @Schema(description = "영화 제목", example = "Inception")
    private String movie_name;

    @Schema(description = "영화 개봉일", example = "2010-07-16")
    private String movie_date;

    @Schema(description = "상영 시간", example = "02:28:00")
    private String movie_time;

    @Schema(description = "감독명", example = "Christopher Nolan")
    private String movie_director;

    @Schema(description = "리뷰 수", example = "1240")
    private Integer movie_review_count;

    @Schema(description = "출연진 리스트", example = "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page")
    private String movie_cast_list;

    @Schema(description = "평점", example = "0.0")
    private Double movie_rating;
}
