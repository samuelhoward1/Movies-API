package com.movies.movies_api.service;

import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.entity.Genre;
import com.movies.movies_api.repository.ActorRepository;
import com.movies.movies_api.repository.GenreRepository;
import com.movies.movies_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    @Autowired
    MovieService(MovieRepository movieRepository, GenreRepository genreRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
    }

    public void addMovie(String title, Integer releaseYear, Integer duration, Long genreId, Set<Long> actorIds) {
        Genre genre = genreRepository.findById(genreId).orElse(null);
        Set<Actor> actors = new HashSet<>();
        for (Long actorId : actorIds) {
            actorRepository.findById(actorId).ifPresent(actors::add);
        }
        if (genre == null) {
            throw new IllegalArgumentException("Genre with ID " + genreId + " not found");
        }
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setReleaseYear(releaseYear);
        movie.setDuration(duration);

        Set<Genre> genreSet = new HashSet<>();
        genreSet.add(genre);
        movie.setGenres(genreSet);

        movie.setActors(actors);
        movieRepository.save(movie);
    }

    public void updateMovie(Long movieId, String title, Integer releaseYear, Integer duration, Long genreId, Set<Long> actorIds) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            throw new IllegalArgumentException("Movie with ID " + movieId + " not found");
        }
        movie.setTitle(title);
        movie.setReleaseYear(releaseYear);
        movie.setDuration(duration);
        Genre genre = genreRepository.findById(genreId).orElse(null);
        if (genre == null) {
            throw new IllegalArgumentException("Genre with ID " + genreId + " not found");
        }

        Set<Genre> genreSet = new HashSet<>();
        genreSet.add(genre);
        movie.setGenres(genreSet);

        Set<Actor> actors = new HashSet<>();
        for (Long actorId : actorIds) {
            actorRepository.findById(actorId).ifPresent(actors::add);
        }
        movie.setActors(actors);
        movieRepository.save(movie);
    }

    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            throw new IllegalArgumentException("Movie with ID " + movieId + " not found");
        }
        movieRepository.delete(movie);
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

    public Set<Actor> getActorsByMovieId(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie with ID " + movieId + " not found"));
        return movie.getActors();
    }
}
