package com.movies.movies_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movies.movies_api.entity.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    // Movie savedMovie = movieRepository.save(movie);
}