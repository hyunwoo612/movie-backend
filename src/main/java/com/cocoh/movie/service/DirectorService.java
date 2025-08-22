package com.cocoh.movie.service;

import com.cocoh.movie.Entity.Director;
import com.cocoh.movie.dto.DirectorDto;
import com.cocoh.movie.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    public Director saveDirector(DirectorDto dto) {

        Director director = Director
                .builder()
                .name(dto.getName())
                .birthDay(dto.getBirthDay())
                .build();

        Director saved = directorRepository.save(director);

        return saved;
    }

}
