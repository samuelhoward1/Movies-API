package com.movies.movies_api.dto;

import java.util.Set;

public class GenreMoviesDTO {

    private Long id;
    private String name;
    private Set<String> movies; // Only movie titles

    // Constructor with all fields
    public GenreMoviesDTO(Long id, String name, Set<String> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getMovies() {
        return movies;
    }

    public void setMovies(Set<String> movies) {
        this.movies = movies;
    }
}



