package com.movies.movies_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "actors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Actor name cannot be blank")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Birthdate cannot be null")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Birthdate must be in ISO 8601 format (YYYY-MM-DD)")
    @Column(name = "birthDate") // Enforce ISO8601 format
    private String birthDate;

    @ManyToMany(mappedBy = "actors")
    @JsonBackReference("movie-actor") // Mark this side as the back-reference (do not serialize)
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
