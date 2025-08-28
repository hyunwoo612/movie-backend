package com.cocoh.movie.repository;

/* Movie 리포지토리 생성 - 최현우 -
수정 17:37 Query 어노테이션 제거 */

import com.cocoh.movie.Entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
//    List<Movie> findByDeletedAtIsNull();
    Movie findByIdAndDeletedAtIsNull(Long id);
    Page<Movie> findByDeletedAtIsNull(Pageable pageable);
    List<Movie> findByMovieNameContainingAndDeletedAtIsNull(String keyword, Pageable pageable);

}
