package com.cocoh.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewsResponseDTO {
    private Long movieId;
    private String movieName;
    private List<ReviewDTO> movieReviewList;
    private Timestamp createdAt;
}
