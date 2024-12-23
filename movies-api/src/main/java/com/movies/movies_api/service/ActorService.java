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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (actorRepository.findByNameIgnoreCase(actor.getName()).isPresent()) {
            throw new DataIntegrityViolationException("An actor with the name '" + actor.getName() + "' already exists.");
        }

        String birthDate = actor.getBirthDate();
        if (!isValidDateFormat(birthDate)) {
            throw new IllegalArgumentException("Birthdate must be in ISO 8601 format (YYYY-MM-DD).");
        }

        try {
            LocalDate parsedDate = LocalDate.parse(birthDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid birthdate: " + birthDate);
        }

        actorRepository.save(actor);

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

    private boolean isValidDateFormat(String date) {
        String dateFormatRegex = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern pattern = Pattern.compile(dateFormatRegex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public Page<Actor> getAllActors(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return actorRepository.findAll(pageable);
    }

    public Set<Actor> getAllActorsWithoutPagination() {
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
        return actor != null ? actor.getMovies() : new HashSet<>();
    }

    public Actor updateActor(Long actorId, String name, String birthDate, Set<Long> movieIds) {
        Actor actor = getActorById(actorId);
        if (actor == null) {
            return null;
        }

        if (name != null) actor.setName(name);
        if (birthDate != null) actor.setBirthDate(birthDate);

        Set<Movie> currentMovies = new HashSet<>(actor.getMovies());

        if (movieIds != null && !movieIds.isEmpty()) {
            Set<Movie> movies = new HashSet<>();
            for (Long movieId : movieIds) {
                movieRepository.findById(movieId).ifPresent(movies::add);
            }
            actor.setMovies(movies);

            for (Movie movie : movies) {
                movie.getActors().add(actor);
                movieRepository.save(movie);
            }

            for (Movie movie : currentMovies) {
                if (!movies.contains(movie)) {
                    movie.getActors().remove(actor);
                    movieRepository.save(movie);
                }
            }
        }

        actorRepository.save(actor);
        return actor;
    }

    public boolean deleteActor(Long actorId, boolean force) {
        Actor actor = getActorById(actorId);
        if (actor != null) {
            if (!force && !actor.getMovies().isEmpty()) {
                throw new IllegalStateException("Unable to delete actor '" + actor.getName() +
                        "' as they are associated with " + actor.getMovies().size() + " movie(s).");
            }

            for (Movie movie : actor.getMovies()) {
                movie.getActors().remove(actor);
                movieRepository.save(movie);
            }

            actorRepository.delete(actor);
            return true;
        }
        return false;
    }
}
