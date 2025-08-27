package com.cocoh.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewRequestDTO {

    @NotBlank
    @Size(min = 3, max = 15)
    @Schema(description = "리뷰 작성자명", example = "김주오")
    private String name;

    private String review;
    private double rating;

}
