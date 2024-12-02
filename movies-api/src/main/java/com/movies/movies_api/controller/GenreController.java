package com.movies.movies_api.controller;

import com.movies.movies_api.dto.GenreMoviesDTO;
import com.movies.movies_api.entity.Genre;
import com.movies.movies_api.service.GenreService;
import com.movies.movies_api.service.MapperService;
import com.movies.movies_api.exception.ResourceNotFoundException;  // import the custom exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Set;

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
    public ResponseEntity<Genre> addGenre(@Valid @RequestBody Genre genre) {
        Genre createdGenre = genreService.createGenre(genre);
        return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Set<Genre>> getAllGenres() {
        Set<Genre> genres = genreService.getAllGenres();
        return new ResponseEntity<>(genres, HttpStatus.OK);
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
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        Genre genre = genreService.getGenre(id);
        if (genre == null) {
            throw new ResourceNotFoundException("Genre not found with id " + id, HttpStatus.NOT_FOUND.toString());
        }
        genreService.deleteGenre(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
