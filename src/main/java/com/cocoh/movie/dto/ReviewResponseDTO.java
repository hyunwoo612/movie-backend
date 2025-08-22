package com.cocoh.movie.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewResponseDTO {
    private Long movieId;
    private String movieName;
    private List<ReviewDTO> movieReviewList;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
