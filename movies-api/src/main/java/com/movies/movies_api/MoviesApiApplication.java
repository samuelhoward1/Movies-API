package com.movies.movies_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoviesApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		System.out.println("Current working directory: " + System.getProperty("user.dir"));
		SpringApplication.run(MoviesApiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// Application logic after startup
		System.out.println("Application started successfully.");
	}
}
