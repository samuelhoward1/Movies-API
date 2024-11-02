package com.movies.movies_api;

import com.movies.movies_api.entity.Movie;
import com.movies.movies_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoviesApiApplication implements CommandLineRunner {

	private final MovieRepository movieRepository;

	@Autowired
	public MoviesApiApplication(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(MoviesApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Test database connection by saving a new movie
		Movie testMovie = new Movie();
		testMovie.setTitle("Test Movie");
		testMovie.setReleaseYear(2022);
		movieRepository.save(testMovie);

		// Fetch the movie from the database
		Movie fetchedMovie = movieRepository.findById(testMovie.getId()).orElse(null);
		if (fetchedMovie != null) {
			System.out.println("Successfully connected to the database! Movie: " + fetchedMovie.getTitle());
		} else {
			System.out.println("Failed to retrieve movie from the database.");
		}
	}
}

