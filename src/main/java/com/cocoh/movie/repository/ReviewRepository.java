package com.cocoh.movie.repository;

import com.cocoh.movie.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByDeletedAtIsNull();
    Review findByIdAndDeletedAtIsNull(Long id);
}
