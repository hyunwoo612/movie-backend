package com.cocoh.movie.repository;

import com.cocoh.movie.Entity.Director;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    List<Director> findByNameContaining(String name, Pageable pageable);
}
