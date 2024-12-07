package com.movies.movies_api.service;

import com.movies.movies_api.entity.Genre;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.repository.GenreRepository;
import com.movies.movies_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    public Genre createGenre(Genre genre) {
        Optional<Genre> existingGenre = genreRepository.findByNameIgnoreCase(genre.getName());
        if (existingGenre.isPresent()) {
            throw new IllegalArgumentException("Genre with name '" + genre.getName() + "' already exists.");
        }
        return genreRepository.save(genre);
    }


    public Page<Genre> getAllGenres(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return genreRepository.findAll(pageable);
    }

    public Genre getGenre(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    public Genre updateGenreName(Long id, String newName) {
        Genre genre = genreRepository.findById(id).orElse(null);
        if (genre != null) {
            genre.setName(newName);
            return genreRepository.save(genre);
        }
        return null;
    }

    public boolean deleteGenre(Long genreId, boolean force) {
        Genre genre = genreRepository.findById(genreId).orElse(null);

        if (genre == null) {
            return false;
        }

        if (!force && !genre.getMovies().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete genre '" + genre.getName() + "' because it has " +
                            genre.getMovies().size() + " associated movie(s)."
            );
        }

        // Force deletion: remove genre from all associated movies
        if (force) {
            for (Movie movie : genre.getMovies()) {
                movie.getGenres().remove(genre);
                movieRepository.save(movie);
            }
        }

        genreRepository.delete(genre);
        return true;
    }


}
