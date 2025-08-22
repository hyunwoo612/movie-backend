package com.cocoh.movie.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewResponseDTO {
    private String name;
    private String review;
    private double rating;
    private Long reviewId;
}
