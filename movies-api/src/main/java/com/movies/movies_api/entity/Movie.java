package com.movies.movies_api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movie title cannot be blank")
    @Size(max = 255, message = "Movie title cannot exceed 255 characters")
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull(message = "Release year cannot be null")
    @Min(value = 1900, message = "Release year must be after 1900")
    @Max(value = 2100, message = "Release year cannot be after 2100")
    @Column(name = "release_year")
    private Integer releaseYear;

    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Column(name = "duration")
    private Integer duration;

    @ManyToMany(mappedBy = "movies")
    // @JsonManagedReference("movie-genre") // Uncomment for serialization
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "movie_actor", // Join table for movie-actor relationship
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    // @JsonManagedReference("movie-actor") // Uncomment for serialization
    private Set<Actor> actors = new HashSet<>();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }
}
