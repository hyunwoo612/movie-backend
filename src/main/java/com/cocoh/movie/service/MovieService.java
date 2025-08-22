package com.cocoh.movie.service;

/* Movie 서비스 생성 - 최현우 -
수정 17:43 레포지토리 수정 후 변수명 변경 */

import com.cocoh.movie.Entity.Movie;
import com.cocoh.movie.dto.MovieListResponse;
import com.cocoh.movie.dto.MovieUpdateRequest;
import com.cocoh.movie.repository.MovieRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieListResponse> findAllMovies() {
        List<Movie> movies = movieRepository.findByDeletedAtIsNull();
        return movies.stream()
                .map(MovieListResponse::from)
                .toList();
    }

    public Movie updateMovie(Long id, MovieUpdateRequest request) {
        Movie movie = movieRepository.findByIdAndDeletedAtIsNull(id);

        if(movie == null) {
            return null;
        }

        movie.setMovie_name(request.getMovie_name());
        movie.setMovie_time(request.getMovie_time());
        movie.setMovie_director(request.getMovie_director());
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
