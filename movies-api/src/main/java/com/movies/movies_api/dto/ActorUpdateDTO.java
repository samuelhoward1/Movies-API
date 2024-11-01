package com.movies.movies_api.dto;

import java.time.LocalDate;
import java.util.Set;

public class ActorUpdateDTO {
    private String name;
    private LocalDate birthDate;
    private Set<Long> movieIds;

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Set<Long> getMovieIds() {
        return movieIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setMovieIds(Set<Long> movieIds) {
        this.movieIds = movieIds;
    }
}

