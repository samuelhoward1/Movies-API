package com.movies.movies_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

public class ActorUpdateDTO {

    @NotBlank(message = "Actor name cannot be blank")
    private String name;

    @NotBlank(message = "Birthdate cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birthdate must follow the format YYYY-MM-DD")
    private String birthDate;

    @NotNull(message = "Movie IDs cannot be null")
    private Set<Long> movieIds;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public Set<Long> getMovieIds() {
        return movieIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setMovieIds(Set<Long> movieIds) {
        this.movieIds = movieIds;
    }
}

