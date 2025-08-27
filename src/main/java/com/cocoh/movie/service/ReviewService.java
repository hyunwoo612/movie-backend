package com.cocoh.movie.service;

import com.cocoh.movie.Entity.Movie;
import com.cocoh.movie.Entity.Review;
import com.cocoh.movie.dto.*;
import com.cocoh.movie.repository.MovieRepository;
import com.cocoh.movie.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
                .name(dto.getName())
                .review(dto.getReview())
                .rating(dto.getRating())
                .movie(movie)
                .build();

        Review savedReview = reviewRepository.save(review);

        movie.getReviews().add(savedReview);

        movie.setMovie_review_count(movie.getReviews().stream()
                .filter(r -> r.getDeletedAt() == null)
                .count());
        movie.setMovie_rating(
                movie.getReviews().stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0.0)
        );

        movieRepository.save(movie);

        return new ReviewDTO(savedReview.getName(), savedReview.getReview(), savedReview.getRating(), savedReview.getId());
    }

    public ReviewResponseDTO getReview(Long movieId, Long reviewId) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(movieId);

        Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId);

        if (movie == null) {
            throw new RuntimeException("Movie not found");
        }

        if (review == null) {
            throw new RuntimeException("Review not found");
        }

        ReviewResponseDTO responseDTO = new ReviewResponseDTO(
                review.getName(),
                review.getReview(),
                review.getRating(),
                reviewId
        );

        return responseDTO;
    }


    public List<ReviewsResponseDTO> getReviews(Long movieId) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(movieId);

        if (movie == null) {
            throw new RuntimeException("Movie not found");
        }

        List<Review> reviews = reviewRepository.findByMovieIdAndDeletedAtIsNull(movieId);

        List<ReviewDTO> reviewDTOList = reviews.stream()
                .map(r -> new ReviewDTO(r.getName(), r.getReview(), r.getRating(), r.getId()))
                .toList();

        ReviewsResponseDTO responseDTO = new ReviewsResponseDTO(
                movie.getId(),
                movie.getMovie_name(),
                reviewDTOList,
                movie.getCreatedAt()
        );

        return List.of(responseDTO);
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


