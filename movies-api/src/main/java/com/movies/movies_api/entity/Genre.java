package com.movies.movies_api.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")

public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "movie_genre", // Name of the join table
            joinColumns = @JoinColumn(name = "genre_id"), // Foreign key in movie_genre referencing Genre
            inverseJoinColumns = @JoinColumn(name = "movie_id") // Foreign key in movie_genre referencing Movie
    )
    private Set<Movie> movies = new HashSet<>();

    public Genre() {

    }

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