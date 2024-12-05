package com.movies.movies_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movies.movies_api.entity.Actor;

import java.util.Optional;
import java.util.Set;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Set<Actor> findByNameContaining(String name);
    Optional<Actor> findByNameIgnoreCase(String name);
}
