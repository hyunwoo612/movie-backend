package com.cocoh.movie.service;

/* Movie 서비스 생성 - 최현우 -
수정 17:43 레포지토리 수정 후 변수명 변경 */

import com.cocoh.movie.Entity.Director;
import com.cocoh.movie.Entity.Image;
import com.cocoh.movie.Entity.Movie;
import com.cocoh.movie.dto.MovieListResponse;
import com.cocoh.movie.dto.MovieRequestDto;
import com.cocoh.movie.dto.MovieResponseDto;
import com.cocoh.movie.dto.MovieUpdateRequest;
import com.cocoh.movie.repository.DirectorRepository;
import com.cocoh.movie.repository.ImageRepository;
import com.cocoh.movie.repository.MovieRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ImageService imageService;
    private final DirectorRepository directorRepository;

    public Movie createMovie(MovieRequestDto dto, List<MultipartFile> file) {
        if (dto.getDirector_id() == null) {
            throw new IllegalArgumentException("director not found");
        }

        Director director = directorRepository.findById(dto.getDirector_id()).orElseThrow(() -> new IllegalArgumentException("director not found"));

        Movie movie = Movie.builder()
                .movie_name(dto.getMovie_name())
                .movie_date(dto.getMovie_date())
                .movie_time(dto.getMovie_time())
                .director(director)
                .movie_cast_list(dto.getMovie_cast_list())
                .movie_genre(dto.getMovie_genre())
                .movie_description(dto.getMovie_description())
                .build();

        movie.setMovie_image(null);

        movieRepository.save(movie);

        String movieitem = "movie";

        if (file != null && !file.isEmpty()) {
            List<String> savedImagePaths = imageService.uploadMovieImage(movie, file, movieitem);
            // 첫 번째 이미지 경로를 movie_image에 저장
            movie.setMovie_image(savedImagePaths.get(0));
            movieRepository.save(movie);
        }

        return movie;
    }

    public List<MovieListResponse> findAllMovies() {
        List<Movie> movies = movieRepository.findByDeletedAtIsNull();
        return movies.stream()
                .map(MovieListResponse::from)
                .toList();
    }

    public MovieResponseDto findMovie(Long id) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(id);
        if (movie == null) {
            throw new RuntimeException("movie not found");
        }

        return new MovieResponseDto(movie);
    }

    public Movie updateMovie(Long id, MovieUpdateRequest request) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(id);

        if(movie == null) {
            return null;
        }

        movie.setMovie_name(request.getMovie_name());
        movie.setMovie_time(request.getMovie_time());

        if (request.getMovie_director() != null) {
            Director director = directorRepository.findById(request.getMovie_director())
                    .orElseThrow(() -> new RuntimeException("Director not found"));
            movie.setDirector(director);
        }

        movie.setMovie_review_count(request.getMovie_review_count());
        movie.setMovie_cast_list(request.getMovie_cast_list());

        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("movie not found"));
        movie.softDelete();
        movieRepository.save(movie);
    }

}
