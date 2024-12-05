package com.movies.movies_api.service;

import com.movies.movies_api.entity.Genre;
import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.dto.GenreMoviesDTO;
import com.movies.movies_api.repository.GenreRepository;
import com.movies.movies_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {

    // Repositories will be injected here
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }


    public Page<Genre> getAllGenres(int page, int size) {
        // Create a PageRequest object using the provided page and size
        PageRequest pageable = PageRequest.of(page, size);

        // Fetch the paginated result from the repository
        return genreRepository.findAll(pageable);
    }


    public Genre getGenre(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

//    public GenreMoviesDTO getMoviesByGenreId(Long genreId) {
//        Optional<Genre> genre = genreRepository.findById(genreId);
//
//        // If genre is found, extract movie titles and return GenreMoviesDTO
//        if (genre.isPresent()) {
//            Set<String> movieTitles = genre.get().getMovies().stream()
//                    .map(movie -> movie.getTitle()) // Get movie title
//                    .collect(Collectors.toSet());  // Collect as Set<String>
//
//            return new GenreMoviesDTO(genre.get().getId(), genre.get().getName(), movieTitles);
//        } else {
//            // Return a default GenreMoviesDTO with empty data or a placeholder
//            return new GenreMoviesDTO(genreId, "Not Found", new HashSet<>());
//        }
//    }

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
            return false; // Genre not found
        }

        if (!force && !genre.getMovies().isEmpty()) {
            // Genre has associated movies, and force is false
            throw new IllegalStateException(
                    "Cannot delete genre '" + genre.getName() + "' because it has " +
                            genre.getMovies().size() + " associated movie(s)."
            );
        }

        // Force deletion: remove genre from all associated movies
        if (force) {
            for (Movie movie : genre.getMovies()) {
                movie.getGenres().remove(genre); // Remove the genre from movie's genres
                movieRepository.save(movie); // Persist the changes in movies
            }
        }

        // Delete the genre
        genreRepository.delete(genre);
        return true;
    }


}
