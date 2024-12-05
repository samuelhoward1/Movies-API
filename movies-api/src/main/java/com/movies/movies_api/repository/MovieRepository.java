package com.movies.movies_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movies.movies_api.entity.Movie;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Set<Movie> findByGenres_Id(Long genreId);
    Set<Movie> findByReleaseYear(Integer releaseYear);
    Set<Movie> findByActors_Id(Long actorId);
    List<Movie> findByTitleContainingIgnoreCase(String title);
    Optional<Movie> findByTitleIgnoreCaseAndReleaseYear(String title, Integer releaseYear);
}
