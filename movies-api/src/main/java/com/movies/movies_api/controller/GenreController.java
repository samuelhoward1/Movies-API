package com.movies.movies_api.controller;

import com.movies.movies_api.dto.GenreMoviesDTO;
import com.movies.movies_api.entity.Genre;
import com.movies.movies_api.service.GenreService;
import com.movies.movies_api.service.MapperService;
import com.movies.movies_api.exception.ResourceNotFoundException;  // import the custom exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;
    private final MapperService mapperService;

    @Autowired
    public GenreController(GenreService genreService, MapperService mapperService) {
        this.genreService = genreService;
        this.mapperService = mapperService;
    }

    @PostMapping
    public ResponseEntity<?> addGenre(@Valid @RequestBody Genre genre) {
        try {
            Genre createdGenre = genreService.createGenre(genre);
            return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGenres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Genre> genresPage = genreService.getAllGenres(page, size);
        return new ResponseEntity<>(genresPage, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        Genre genre = genreService.getGenre(id);
        if (genre == null) {
            throw new ResourceNotFoundException("Genre not found with id " + id, HttpStatus.NOT_FOUND.toString());
        }
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestParam String newName) {
        Genre updatedGenre = genreService.updateGenreName(id, newName);
        if (updatedGenre == null) {
            throw new ResourceNotFoundException("Genre not found with id " + id, HttpStatus.NOT_FOUND.toString());
        }
        return new ResponseEntity<>(updatedGenre, HttpStatus.OK);
    }

    @GetMapping("/{id}/movies")
    public ResponseEntity<GenreMoviesDTO> getMoviesByGenre(@PathVariable Long id) {
        Genre genre = genreService.getGenre(id);
        if (genre == null) {
            throw new ResourceNotFoundException("Genre not found with id " + id, HttpStatus.NOT_FOUND.toString());
        }
        GenreMoviesDTO dto = mapperService.toGenreMoviesDTO(genre);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        try {
            boolean deleted = genreService.deleteGenre(id, force);

            // If the genre is not found, throw an exception
            if (!deleted) {
                throw new ResourceNotFoundException("Genre not found with id " + id, HttpStatus.NOT_FOUND.toString());
            }

            // Return 204 No Content if deletion is successful
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException e) {
            // Handle the case where the genre cannot be deleted due to associated movies
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
