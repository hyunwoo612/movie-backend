package com.cocoh.movie.repository;

import com.cocoh.movie.Entity.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {
}
