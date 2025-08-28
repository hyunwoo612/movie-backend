package com.cocoh.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewsResponseDTO {
    private Long movieId;
    private String movieName;
    private Page<ReviewDTO> movieReviewList;
    private Timestamp createdAt;
}
