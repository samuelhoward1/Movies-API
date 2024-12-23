package com.movies.movies_api.controller;

import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.service.MovieService;
import com.movies.movies_api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
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
    public ResponseEntity<?> addMovie(@Valid @RequestBody Movie movie) {
        try {
            Movie createdMovie = movieService.addMovie(movie);
            return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMovies(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null || size == null) {
            Set<Movie> allMovies = movieService.getAllMoviesWithoutPagination();
            return new ResponseEntity<>(allMovies, HttpStatus.OK);
        } else {
            Page<Movie> moviesPage = movieService.getAllMovies(PageRequest.of(page, size));
            return new ResponseEntity<>(moviesPage, HttpStatus.OK);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMoviesByTitle(@RequestParam String title) {
        List<Movie> movies = movieService.searchMoviesByTitle(title);
        if (movies.isEmpty()) {
            throw new ResourceNotFoundException("No movies found matching title: " + title, HttpStatus.NOT_FOUND.toString());
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            throw new ResourceNotFoundException("Movie not found with id " + id, HttpStatus.NOT_FOUND.toString());
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        Movie updatedMovie = movieService.updateMovie(id, movie);
        if (updatedMovie == null) {
            throw new ResourceNotFoundException("Movie not found with id " + id, HttpStatus.NOT_FOUND.toString());
        }
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        try {
            boolean deleted = movieService.deleteMovie(id, force);
            if (!deleted) {
                throw new ResourceNotFoundException("Movie not found with id " + id, HttpStatus.NOT_FOUND.toString());
            }
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(params = "year")
    public ResponseEntity<Set<Movie>> getMoviesByYear(@RequestParam Integer year) {
        Set<Movie> movies = movieService.getMoviesByReleaseYear(year);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping(params = "actorId")
    public ResponseEntity<Set<Movie>> getMoviesByActor(@RequestParam("actorId") Long actorId) {
        Set<Movie> movies = movieService.getMoviesByActorId(actorId);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{movieId}/actors")
    public ResponseEntity<Set<Actor>> getActorsByMovieId(@PathVariable Long movieId) {
        Set<Actor> actors = movieService.getActorsByMovieId(movieId);
        if (actors == null || actors.isEmpty()) {
            throw new ResourceNotFoundException("No actors found for movie with id " + movieId, HttpStatus.NOT_FOUND.toString());
        }
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }
}
