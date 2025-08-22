package com.cocoh.movie.service;

import com.cocoh.movie.Entity.Movie;
import com.cocoh.movie.Entity.Review;
import com.cocoh.movie.dto.*;
import com.cocoh.movie.repository.MovieRepository;
import com.cocoh.movie.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    public ReviewDTO saveReview(Long movieId, ReviewRequestDTO dto) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(movieId);

        if (movie == null) {
            throw new RuntimeException("Movie not found");
        }

        Review review = Review.builder()
                .review(dto.getReview())
                .rating(dto.getRating())
                .movie(movie)
                .build();

        Review savedReview = reviewRepository.save(review);

        movie.getReviews().add(savedReview);

        movie.setMovie_review_count(movie.getReviews().size());
        movie.setMovie_rating(
                movie.getReviews().stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0.0)
        );

        movieRepository.save(movie);

        return new ReviewDTO(savedReview.getReview(), savedReview.getRating());
    }

    public ApiResponse<ReviewResponseDTO> getReview(Long movieId, Long reviewId) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(movieId);

        Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId);

        if (movie == null) {
            throw new RuntimeException("Movie not found");
        }

        if (review == null) {
            throw new RuntimeException("Review not found");
        }

        ReviewDTO reviewDTO = new ReviewDTO(review.getReview(), review.getRating());

        ReviewResponseDTO responseDTO = new ReviewResponseDTO(
                movie.getId(),
                movie.getMovie_name(),
                List.of(reviewDTO), // 단일 리뷰도 리스트로 감싸기
                movie.getCreatedAt(),
                movie.getUpdatedAt()
        );

        return new ApiResponse<>(
                200,
                responseDTO
        );
    }


    public List<ReviewsResponseDTO> getReviews() {
        List<Movie> movies = movieRepository.findByDeletedAtIsNull();

        List<Review> reviews = reviewRepository.findByDeletedAtIsNull();

        if (movies == null) {
            return new ArrayList<>();
        }

        List<ReviewsResponseDTO> response = movies.stream()
                .map(movie -> {
                    List<ReviewDTO> reviewDTOs = reviews.stream()
                            .filter(r -> r.getMovie().getId().equals(movie.getId()))
                            .map(r -> new ReviewDTO(r.getReview(), r.getRating()))
                            .toList();

                    return new ReviewsResponseDTO(
                            movie.getId(),
                            movie.getMovie_name(),
                            reviewDTOs,
                            movie.getCreatedAt()
                    );
                })
                .toList();

        try {
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Review updateReview(Long movieId, Long reviewId, ReviewRequestDTO request) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(movieId);
        Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId);

        if(movie == null) {
            throw new RuntimeException("Movie not found");
        }

        if(review == null) {
            throw new RuntimeException("Review not found");
        }

        if(!review.getMovie().getId().equals(movie.getId())) {
            throw new RuntimeException("Movie not found");
        }

        review.setReview(request.getReview());
        review.setRating(request.getRating());

        return reviewRepository.save(review);
    }

    public void deleteReview(Long movieId, Long reviewId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));

        if(!review.getMovie().getId().equals(movie.getId())) {
            throw new RuntimeException("Movie not found");
        }

        review.softDelete();
        reviewRepository.save(review);
    }

}


