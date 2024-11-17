package com.movies.movies_api.service;

import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.repository.ActorRepository;
import com.movies.movies_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Add Actor and update the movie's actors set
    public void addActor(Actor actor) {
        // Save the actor
        actorRepository.save(actor);

        // Now update the movie's actors set
        if (actor.getMovies() != null) {
            for (Movie movie : actor.getMovies()) {
                Optional<Movie> dbMovie = movieRepository.findById(movie.getId());
                if (dbMovie.isPresent()) {
                    Movie fetchedMovie = dbMovie.get();
                    fetchedMovie.getActors().add(actor); // Add the actor to the movie's actors set
                    movieRepository.save(fetchedMovie); // Save the movie to persist the updated actors set
                }
            }
        }
    }

    // Get all actors
    public Set<Actor> getAllActors() {
        return new HashSet<>(actorRepository.findAll());
    }

    // Get actor by ID
    public Actor getActorById(Long actorId) {
        return actorRepository.findById(actorId).orElse(null);
    }

    // Get actors by name
    public Set<Actor> getActorsByName(String name) {
        return actorRepository.findByNameContaining(name);
    }

    // Get movies by actor ID
    public Set<Movie> getMoviesByActorId(Long actorId) {
        Actor actor = getActorById(actorId);
        return actor != null ? actor.getMovies() : new HashSet<>();
    }

    // Update Actor with movies and update the movie's actors set
    public Actor updateActor(Long actorId, String name, String birthDate, Set<Long> movieIds) {
        Actor actor = getActorById(actorId);
        if (actor == null) {
            return null;
        }

        // Update actor fields
        if (name != null) actor.setName(name);
        if (birthDate != null) actor.setBirthDate(birthDate);

        // Get the current list of movies the actor is associated with
        Set<Movie> currentMovies = new HashSet<>(actor.getMovies());

        // Update actor's movies
        if (movieIds != null && !movieIds.isEmpty()) {
            Set<Movie> movies = new HashSet<>();
            for (Long movieId : movieIds) {
                movieRepository.findById(movieId).ifPresent(movies::add);
            }
            actor.setMovies(movies);

            // Now update the movie's actors set
            for (Movie movie : movies) {
                movie.getActors().add(actor); // Add the actor to the movie's actors set
                movieRepository.save(movie); // Save the movie to persist the updated actors set
            }

            // Remove actor from old movies if they are no longer in the updated list
            for (Movie movie : currentMovies) {
                if (!movies.contains(movie)) {
                    movie.getActors().remove(actor); // Remove the actor from the movie's actors set
                    movieRepository.save(movie); // Save the movie to persist the updated actors set
                }
            }
        }

        // Save the updated actor
        actorRepository.save(actor);
        return actor;
    }

    // Delete Actor
    public boolean deleteActor(Long actorId) {
        Actor actor = getActorById(actorId);
        if (actor != null) {
            // Remove actor from all movies before deleting
            for (Movie movie : actor.getMovies()) {
                movie.getActors().remove(actor); // Remove the actor from the movie's actors set
                movieRepository.save(movie); // Save the movie to persist the updated actors set
            }
            actorRepository.delete(actor);
            return true;
        } else {
            return false;
        }
    }
}
