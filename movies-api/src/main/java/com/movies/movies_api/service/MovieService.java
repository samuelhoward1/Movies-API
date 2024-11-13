package com.movies.movies_api.service;

import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.entity.Genre;
import com.movies.movies_api.repository.ActorRepository;
import com.movies.movies_api.repository.GenreRepository;
import com.movies.movies_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
        // Fetch all genres by their IDs
        Set<Genre> genres = movie.getGenres().stream()
                .map(g -> genreRepository.findById(g.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Genre with ID " + g.getId() + " not found")))
                .collect(Collectors.toSet());

        // Fetch all actors by their IDs
        Set<Actor> actors = movie.getActors().stream()
                .map(a -> actorRepository.findById(a.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Actor with ID " + a.getId() + " not found")))
                .collect(Collectors.toSet());

        // Set the genres and actors to the movie
        movie.setGenres(genres);
        movie.setActors(actors);

        // Update each genre's movie set to include this movie
        for (Genre genre : genres) {
            genre.getMovies().add(movie); // Add the movie to the genre's movies set
        }

        // Save the movie (this should also update the join table due to cascading)
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long movieId, Movie updatedMovie) {
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie with ID " + movieId + " not found"));

        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        existingMovie.setDuration(updatedMovie.getDuration());

        Genre genre = updatedMovie.getGenres().stream()
                .findFirst()
                .map(g -> genreRepository.findById(g.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Genre with ID " + g.getId() + " not found")))
                .orElseThrow(() -> new IllegalArgumentException("No genre provided"));
        existingMovie.setGenres(Set.of(genre));

        Set<Actor> actors = new HashSet<>();
        if (updatedMovie.getActors() != null) {
            for (Actor actor : updatedMovie.getActors()) {
                actors.add(actorRepository.findById(actor.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Actor with ID " + actor.getId() + " not found")));
            }
        }
        existingMovie.setActors(actors);

        return movieRepository.save(existingMovie);
    }

    public boolean deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            return false;
        }
        movieRepository.delete(movie);
        return true;
    }

    public Set<Movie> getAllMovies() {
        return new HashSet<>(movieRepository.findAll());
    }

    public Movie getMovieById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie with ID " + movieId + " not found"));
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
                .orElseThrow(() -> new IllegalArgumentException("Movie with ID " + movieId + " not found"));
        return movie.getActors();
    }
}
