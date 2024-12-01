package com.movies.movies_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Genre name cannot be blank")
    @Size(max = 100, message = "Genre name must not exceed 100 characters")
    @Column(name = "name", nullable = false, unique = true) // Enforce unique constraint in DB
    private String name;

    @ManyToMany
    @JoinTable(
            name = "movie_genre", // Name of the join table
            joinColumns = @JoinColumn(name = "genre_id"), // Foreign key referencing Genre
            inverseJoinColumns = @JoinColumn(name = "movie_id") // Foreign key referencing Movie
    )
    @JsonBackReference("movie-genre") // Mark this side as the back-reference (do not serialize)
    private Set<Movie> movies = new HashSet<>();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
