package com.movies.movies_api.service;

import com.movies.movies_api.dto.GenreMoviesDTO;
import com.movies.movies_api.entity.Genre;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class MapperService {

    public GenreMoviesDTO toGenreMoviesDTO(Genre genre) {
        return new GenreMoviesDTO(
                genre.getId(),
                genre.getName(),
                genre.getMovies().stream()
                        .map(movie -> movie.getTitle())
                        .collect(Collectors.toSet())
        );
    }
}


