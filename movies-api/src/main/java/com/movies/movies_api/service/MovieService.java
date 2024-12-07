package com.movies.movies_api.service;

import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.entity.Genre;
import com.movies.movies_api.repository.ActorRepository;
import com.movies.movies_api.repository.GenreRepository;
import com.movies.movies_api.repository.MovieRepository;
import com.movies.movies_api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    @Autowired
    MovieService(MovieRepository movieRepository, GenreRepository genreRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
    }

    @Transactional
    public Movie addMovie(Movie movie) {
        Optional<Movie> existingMovie = movieRepository.findByTitleIgnoreCaseAndReleaseYear(movie.getTitle(), movie.getReleaseYear());
        if (existingMovie.isPresent()) {
            throw new IllegalArgumentException("Movie with title '" + movie.getTitle() + "' and release year " + movie.getReleaseYear() + " already exists.");
        }

        Set<Genre> genres = movie.getGenres().stream()
                .map(g -> genreRepository.findById(g.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + g.getId() + " not found", String.valueOf(g.getId()))))
                .collect(Collectors.toSet());

        Set<Actor> actors = movie.getActors().stream()
                .map(a -> actorRepository.findById(a.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + a.getId() + " not found", String.valueOf(a.getId()))))
                .collect(Collectors.toSet());

        movie.setGenres(genres);
        movie.setActors(actors);

        for (Genre genre : genres) {
            genre.getMovies().add(movie);
        }

        return movieRepository.save(movie);
    }


    @Transactional
    public Movie updateMovie(Long movieId, Movie updatedMovie) {
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " not found", String.valueOf(movieId)));

        // Only update fields that are not null
        if (updatedMovie.getTitle() != null) {
            existingMovie.setTitle(updatedMovie.getTitle());
        }
        if (updatedMovie.getReleaseYear() != null) {
            existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        }
        if (updatedMovie.getDuration() != null) {
            existingMovie.setDuration(updatedMovie.getDuration());
        }

        if (updatedMovie.getGenres() != null && !updatedMovie.getGenres().isEmpty()) {
            // Map the provided genres by their IDs
            Set<Genre> newGenres = updatedMovie.getGenres().stream()
                    .map(g -> genreRepository.findById(g.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + g.getId() + " not found", String.valueOf(g.getId()))))
                    .collect(Collectors.toSet());

            // Clear existing genres and add the new ones
            Set<Genre> oldGenres = new HashSet<>(existingMovie.getGenres());
            existingMovie.getGenres().clear();
            existingMovie.getGenres().addAll(newGenres);

            // Synchronize the inverse side of the relationship (Genre -> Movie)
            for (Genre genre : newGenres) {
                genre.getMovies().add(existingMovie);
            }

            // Remove the movie from the old genres that are no longer associated
            for (Genre genre : oldGenres) {
                if (!newGenres.contains(genre)) {
                    genre.getMovies().remove(existingMovie);
                }
            }
        }

        if (updatedMovie.getActors() != null && !updatedMovie.getActors().isEmpty()) {
            Set<Actor> newActors = updatedMovie.getActors().stream()
                    .map(a -> actorRepository.findById(a.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + a.getId() + " not found", String.valueOf(a.getId()))))
                    .collect(Collectors.toSet());

            // Clear existing actors and add the new ones
            existingMovie.getActors().clear();
            existingMovie.getActors().addAll(newActors);

            // Synchronize the inverse side of the relationship (Actor -> Movie)
            for (Actor actor : newActors) {
                actor.getMovies().add(existingMovie);
            }
        }

        return movieRepository.save(existingMovie);
    }

    public boolean deleteMovie(Long movieId, boolean force) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Movie with ID " + movieId + " not found", String.valueOf(movieId))
                );

        // Check for associated entities (e.g., genres or actors)
        if (!force && (!movie.getGenres().isEmpty() || !movie.getActors().isEmpty())) {
            throw new IllegalStateException(
                    "Cannot delete movie '" + movie.getTitle() + "' because it is associated with " +
                            movie.getGenres().size() + " genre(s) and " +
                            movie.getActors().size() + " actor(s)."
            );
        }

        // If force is true, clean up relationships
        if (force) {
            for (Genre genre : movie.getGenres()) {
                genre.getMovies().remove(movie);
                genreRepository.save(genre);
            }
            for (Actor actor : movie.getActors()) {
                actor.getMovies().remove(movie);
                actorRepository.save(actor);
            }
        }

        movieRepository.delete(movie);
        return true;
    }


    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }



    public Movie getMovieById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " not found", String.valueOf(movieId)));
    }

    public Set<Movie> getMoviesByGenre(Long genreId) {
        return movieRepository.findByGenres_Id(genreId);
    }

    public Set<Movie> getMoviesByReleaseYear(Integer releaseYear) {
        return movieRepository.findByReleaseYear(releaseYear);
    }

    public Set<Movie> getMoviesByActorId(Long actorId) {
        return movieRepository.findByActors_Id(actorId);
    }

    public Set<Actor> getActorsByMovieId(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " not found", String.valueOf(movieId)));
        return movie.getActors();
    }
}
