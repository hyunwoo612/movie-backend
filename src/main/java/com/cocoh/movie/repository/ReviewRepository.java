package com.cocoh.movie.repository;

import com.cocoh.movie.Entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByMovieIdAndDeletedAtIsNull(Long movieId, Pageable pageable);
    Review findByIdAndDeletedAtIsNull(Long id);
}
