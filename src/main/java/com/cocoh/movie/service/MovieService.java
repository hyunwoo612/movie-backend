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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                .movieName(dto.getMovie_name())
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

    public Page<MovieListResponse> findAllMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Movie> movies = movieRepository.findByDeletedAtIsNull(pageable);

        return movies.map(MovieListResponse::from);
    }

//    public Page<MovieListResponse> findByDeletedAtIsNullContaining(String keyword, Pageable pageable) {
//
//    }

    public MovieResponseDto findMovie(Long id) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(id);
        if (movie == null) {
            throw new RuntimeException("movie not found");
        }

        return new MovieResponseDto(movie);
    }

    public Movie updateMovie(Long id, MovieUpdateRequest request, List<MultipartFile> file) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(id);

        if (request.getDirector_id() == null) {
            throw new IllegalArgumentException("director not found");
        }

        Director director = directorRepository.findById(request.getDirector_id()).orElseThrow(() -> new IllegalArgumentException("director not found"));

        if(movie == null) {
            return null;
        }

        movie.setMovieName(request.getMovie_name());
        movie.setMovie_time(request.getMovie_time());

        if (request.getMovie_director() != null) {
            movie.setDirector(director);
        }

        movie.setMovie_cast_list(request.getMovie_cast_list());

        movie.setMovie_image(null);

        movieRepository.save(movie);

        String movieitem = "movie";

        if (file != null && !file.isEmpty()) {
            List<String> savedImagePaths = imageService.uploadMovieImage(movie, file, movieitem);
            // 첫 번째 이미지 경로를 movie_image에 저장
            movie.setMovie_image(savedImagePaths.get(0));
            movieRepository.save(movie);
        }

        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("movie not found"));
        movie.softDelete();
        movieRepository.save(movie);
    }

    public List<Movie> searchMovie(String keyword, Pageable pageable) {
        List<Movie> movieList = movieRepository.findByMovieNameContainingAndDeletedAtIsNull(keyword, pageable);

        return movieList;
    }

}
