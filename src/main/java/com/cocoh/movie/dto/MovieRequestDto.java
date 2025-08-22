package com.cocoh.movie.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.SqlTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "영화 등록 요청")
public class MovieRequestDto {

    @Schema(description = "영화 제목", example = "Inception")
    private String movie_name;

    @Schema(description = "영화 개봉일", example = "2010-07-16")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate movie_date;

    @Schema(description = "상영 시간", example = "02:28:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime movie_time;

    @Schema(description = "감독명", example = "Christopher Nolan")
    private String movie_director;

    @Schema(description = "출연진 리스트", example = "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page")
    private String movie_cast_list;

    @Schema(description = "장르", example = "로맨스, 코미디")
    private String movie_genre;

    @Schema(description = "영화 설명", example = "이 영화는 옛날 옛적 부터...")
    private String movie_description;

    private String movie_image;
}
