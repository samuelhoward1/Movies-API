package com.movies.movies_api.service;

import com.movies.movies_api.entity.Genre;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.repository.GenreRepository;
import com.movies.movies_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GenreService {

    // Repository will be injected here
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    public Genre createGenre(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        return genreRepository.save(genre);
    }

    public Set<Genre> getAllGenres() {
        List<Genre> genreList = genreRepository.findAll();
        return new HashSet<>(genreList);
    }

    public Genre getGenre(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    public Set<Movie> getMoviesByGenre(Long genreId) {
        return new HashSet<>(movieRepository.findByGenres_Id(genreId));
    }

    public Genre updateGenreName(Long id, String newName) {
        Genre genre = genreRepository.findById(id).orElse(null);
        if (genre != null) {
            genre.setName(newName);
            return genreRepository.save(genre);
        }
        return null;
    }

    public void  deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id).orElse(null);
        if (genre != null) {
            genreRepository.delete(genre);
        }
    }
}
