package com.movies.movies_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movies.movies_api.entity.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    // Movie savedMovie = movieRepository.save(movie);
    Optional<Genre> findByNameIgnoreCase(String name);
}
