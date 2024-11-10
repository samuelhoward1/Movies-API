package com.movies.movies_api.service;

import com.movies.movies_api.dto.GenreMoviesDTO;
import com.movies.movies_api.entity.Genre;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class MapperService {

    public GenreMoviesDTO toGenreMoviesDTO(Genre genre) {
        // Using the constructor with parameters for GenreMoviesDTO
        return new GenreMoviesDTO(
                genre.getId(),
                genre.getName(),
                genre.getMovies().stream()
                        .map(movie -> movie.getTitle())  // Fetch movie titles
                        .collect(Collectors.toSet())
        );
    }
}


