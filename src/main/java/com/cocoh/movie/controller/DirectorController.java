package com.cocoh.movie.controller;

import com.cocoh.movie.Entity.Director;
import com.cocoh.movie.dto.DirectorDto;
import com.cocoh.movie.repository.DirectorRepository;
import com.cocoh.movie.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Director")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    private final DirectorRepository directorRepository;

    @GetMapping
    public ResponseEntity<List<Director>> getDirector() {
        List<Director> directors = directorRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(directors);
    }

    @PostMapping
    public ResponseEntity<Director> saveDirector(@RequestBody DirectorDto dto) {
        Director save = directorService.saveDirector(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

}
