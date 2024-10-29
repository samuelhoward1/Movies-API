package com.movies.movies_api.service;

import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.repository.ActorRepository;
import com.movies.movies_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
class ActorService {
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    @Autowired
    ActorService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    public void addActor(String name, LocalDate birthDate) {
        Actor actor = new Actor();
        actor.setName(name);
        actor.setBirthDate(birthDate);
        actorRepository.save(actor);
    }

    public Set<Actor> getAllActors() {
        return new HashSet<>(actorRepository.findAll());
    }

    public Actor getActorById(Long actorId) {
        return actorRepository.findById(actorId)
                .orElseThrow(() -> new IllegalArgumentException("Actor with ID " + actorId + " not found"));
    }

    public Set<Actor> getActorsByName(String name) {
        return actorRepository.findByNameContaining(name);
    }

    public Set<Movie> getMoviesByActorId(Long actorId) {
        Actor actor = getActorById(actorId);
        return actor.getMovies();
    }

    public void updateActor(Long actorId, Optional<String> name, Optional<LocalDate> birthDate, Set<Long> movieIds) {
        Actor actor = getActorById(actorId);
        name.ifPresent(actor::setName);
        birthDate.ifPresent(actor::setBirthDate);

        if (movieIds != null && !movieIds.isEmpty()) {
            Set<Movie> movies = new HashSet<>();
            for (Long movieId : movieIds) {
                movieRepository.findById(movieId).ifPresent(movies::add);
            }
            actor.setMovies(movies);
        }

        actorRepository.save(actor);
    }

    public void deleteActor(Long actorId) {
        Actor actor = getActorById(actorId);
        actorRepository.delete(actor);
    }
}
