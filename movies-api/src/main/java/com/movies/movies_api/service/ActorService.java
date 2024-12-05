package com.movies.movies_api.service;

import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.repository.ActorRepository;
import com.movies.movies_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.dao.DataIntegrityViolationException;

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
        // Check if an actor with the same name exists
        if (actorRepository.findByNameIgnoreCase(actor.getName()).isPresent()) {
            throw new DataIntegrityViolationException("An actor with the name '" + actor.getName() + "' already exists.");
        }

        // Save the actor
        actorRepository.save(actor);

        // Update the movie's actors set
        if (actor.getMovies() != null) {
            for (Movie movie : actor.getMovies()) {
                Optional<Movie> dbMovie = movieRepository.findById(movie.getId());
                if (dbMovie.isPresent()) {
                    Movie fetchedMovie = dbMovie.get();
                    fetchedMovie.getActors().add(actor);
                    movieRepository.save(fetchedMovie);
                }
            }
        }
        }

    public Page<Actor> getAllActors(int page, int size) {
        // Create a PageRequest object using the provided page and size
        PageRequest pageable = PageRequest.of(page, size);

        // Fetch paginated actors
        return actorRepository.findAll(pageable);
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
    public boolean deleteActor(Long actorId, boolean force) {
        Actor actor = getActorById(actorId);
        if (actor != null) {
            // If not forced and actor is associated with movies, throw an error with actor name and movie count
            if (!force && !actor.getMovies().isEmpty()) {
                throw new IllegalStateException("Unable to delete actor '" + actor.getName() +
                        "' as they are associated with " + actor.getMovies().size() + " movie(s).");
            }

            // Force deletion: remove actor from all movies
            for (Movie movie : actor.getMovies()) {
                movie.getActors().remove(actor);
                movieRepository.save(movie);
            }

            // Delete the actor
            actorRepository.delete(actor);
            return true;
        }
        return false; // Actor not found
    }



}
