package com.movies.movies_api.controller;

import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie createdMovie = movieService.addMovie(movie);
        return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Set<Movie>> getAllMovies() {
        Set<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return movie != null ? new ResponseEntity<>(movie, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        Movie updatedMovie = movieService.updateMovie(id, movie);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        if (movieService.deleteMovie(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(params = "genre")
    public ResponseEntity<Set<Movie>> getMoviesByGenre(@RequestParam Long genreId) {
        Set<Movie> movies = movieService.getMoviesByGenre(genreId);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping(params = "year")
    public ResponseEntity<Set<Movie>> getMoviesByYear(@RequestParam Integer year) {
        Set<Movie> movies = movieService.getMoviesByReleaseYear(year);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping(params = "actor")
    public ResponseEntity<Set<Movie>> getMoviesByActor(@RequestParam Long actorId) {
        Set<Movie> movies = movieService.getMoviesByActorId(actorId);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{movieId}/actors")
    public ResponseEntity<Set<Actor>> getActorsByMovieId(@PathVariable Long movieId) {
        Set<Actor> actors = movieService.getActorsByMovieId(movieId);
        return actors != null ? new ResponseEntity<>(actors, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
