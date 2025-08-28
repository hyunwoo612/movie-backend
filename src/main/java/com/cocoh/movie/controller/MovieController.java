package com.cocoh.movie.controller;

/* 2025-08-19 영화 등록, 조회 생성 */

import com.cocoh.movie.Entity.Movie;
import com.cocoh.movie.dto.MovieListResponse;
import com.cocoh.movie.dto.MovieRequestDto;
import com.cocoh.movie.dto.MovieResponseDto;
import com.cocoh.movie.dto.MovieUpdateRequest;
import com.cocoh.movie.repository.MovieRepository;
import com.cocoh.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Movie", description = "영화 게시판 입니다.")
@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @Operation(summary = "영화 게시판 목록 조회", description = "영화 게시판의 전체 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<Page<MovieListResponse>> listMovies(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Page<MovieListResponse> movie = movieService.findAllMovies(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @Operation(summary = "영화 게시판 목록 검색 조회", description = "영화 게시판의 전체 목록을 검색 조회합니다.")
    @GetMapping("/list/search")
    public ResponseEntity<List<Movie>> listSearchMovies(String keyword, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        List<Movie> searchList = movieService.searchMovie(keyword, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(searchList);
    }

    @Operation(summary = "영화 게시판 상세 조회", description = "영화 게시판을 상세 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovie(@PathVariable Long id) {
        MovieResponseDto movie = movieService.findMovie(id);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @Operation(summary = "영화 게시판 작성", description = "새로운 영화를 게시판에 게시합니다.")
    @PostMapping
    public ResponseEntity<Movie> save(@ModelAttribute MovieRequestDto movieDto, @RequestParam(value = "images", required = false) List<MultipartFile> images) {  // DTO → Entity
        Movie saved = movieService.createMovie(movieDto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "영화 게시판 수정", description = "영화 게시판을 수정 합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Movie> update(@PathVariable Long id, @ModelAttribute MovieUpdateRequest movieUpdateRequest, @RequestParam(value = "images", required = false) List<MultipartFile> file) {
        Movie updateMovie = movieService.updateMovie(id, movieUpdateRequest, file);
        return ResponseEntity.ok(updateMovie);
    }

    @Operation(summary = "영화 게시판 삭제", description = "영화 게시판을 삭제 합니다.")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

}
