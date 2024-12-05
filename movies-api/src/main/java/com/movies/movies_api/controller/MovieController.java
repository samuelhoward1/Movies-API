package com.movies.movies_api.controller;

import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.service.MovieService;
import com.movies.movies_api.exception.ResourceNotFoundException;  // import the custom exception
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
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        Movie createdMovie = movieService.addMovie(movie);
        return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Movie>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Movie> movies = movieService.getAllMovies(PageRequest.of(page, size));
        return new ResponseEntity<>(movies, HttpStatus.OK);
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
            // Attempt to delete the movie
            boolean deleted = movieService.deleteMovie(id, force);

            // If the movie is not found, throw an exception
            if (!deleted) {
                throw new ResourceNotFoundException("Movie not found with id " + id, HttpStatus.NOT_FOUND.toString());
            }

            // Return 204 No Content if deletion is successful
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException e) {
            // Handle the case where the movie cannot be deleted due to associated entities
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
