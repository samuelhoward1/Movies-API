package com.movies.movies_api.service;

import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.repository.ActorRepository;
import com.movies.movies_api.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ActorService {
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    @Autowired
    ActorService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    public void addActor(Actor actor) {
        actorRepository.save(actor);
    }

    public Set<Actor> getAllActors() {
        return new HashSet<>(actorRepository.findAll());
    }

    public Actor getActorById(Long actorId) {
        return actorRepository.findById(actorId).orElse(null);
    }

    public Set<Actor> getActorsByName(String name) {
        return actorRepository.findByNameContaining(name);
    }

    public Set<Movie> getMoviesByActorId(Long actorId) {
        Actor actor = getActorById(actorId);
        return actor.getMovies();
    }

    public Actor updateActor(Long actorId, String name, String birthDate, Set<Long> movieIds) {
        Actor actor = getActorById(actorId);
        if (actor == null) {
            return null;
        }

        if (name != null) actor.setName(name);
        if (birthDate != null) actor.setBirthDate(birthDate);
        if (movieIds != null && !movieIds.isEmpty()) {
            Set<Movie> movies = new HashSet<>();
            for (Long movieId : movieIds) {
                movieRepository.findById(movieId).ifPresent(movies::add);
            }
            actor.setMovies(movies);
        }
        actorRepository.save(actor);
        return actor;
    }


    public boolean deleteActor(Long actorId) {
        Actor actor = getActorById(actorId);
        if (actor != null) {
            actorRepository.delete(actor);
            return true;
        } else {
            return false;
        }
    }

}
