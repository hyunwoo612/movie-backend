package com.cocoh.movie.controller;

import com.cocoh.movie.Entity.Review;
import com.cocoh.movie.dto.*;
import com.cocoh.movie.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@Tag(name = "Review", description = "리뷰 게시판 입니다.")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "특정 영화의 특정 리뷰 조회", description = "movieId와 reviewId를 이용하여 해당 영화의 리뷰 상세 정보를 조회합니다.")
    @GetMapping("/{movieId}")
    public ResponseEntity<ReviewResponseDTO> getReviews(
            @PathVariable Long movieId,
            @RequestParam("reviewId") Long reviewId) {
        try {
            ReviewResponseDTO data = reviewService.getReview(movieId, reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(data);
        } catch (Exception e) {
            log.error(e.getMessage()); // log.info & log.error를 잘 이용하여 유지, 보수성을 높이는 것을 목적으로 한다.
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "전체 리뷰 목록 조회", description = "삭제되지 않은 모든 영화의 리뷰를 반환합니다.")
    @GetMapping("/all/{movieId}")
    public ResponseEntity<List<ReviewsResponseDTO>> getAllReviews(@PathVariable Long movieId, @RequestParam int page, @RequestParam int size) {
        try {
            List<ReviewsResponseDTO> data = reviewService.getReviews(movieId, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "리뷰 작성", description = "특정 영화(movieId)에 새로운 리뷰를 작성합니다.")
    @PostMapping("/{movieId}")
    public ResponseEntity<ReviewDTO> save(@PathVariable Long movieId, @RequestBody @Valid ReviewRequestDTO dto) {
        try {
            ReviewDTO response = reviewService.saveReview(movieId, dto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "리뷰 수정", description = "특정 영화의 특정 리뷰를 수정합니다.")
    @PutMapping("/{movieId}")
    public ResponseEntity<Review> update(
            @PathVariable Long movieId,
            @RequestParam("reviewId") Long reviewId,
            @RequestBody ReviewRequestDTO dto) {
        try {
            Review response = reviewService.updateReview(movieId, reviewId, dto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "리뷰 삭제", description = "특정 영화의 특정 리뷰를 soft delete 처리합니다.")
    @PatchMapping("/{movieId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long movieId,
            @RequestParam("reviewId") Long reviewId) {
        try {
            reviewService.deleteReview(movieId, reviewId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
