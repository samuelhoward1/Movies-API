package com.movies.movies_api.controller;

import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.service.ActorService;
import com.movies.movies_api.dto.ActorUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PostMapping
    public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) {
        actorService.addActor(actor);
        return new ResponseEntity<>(actor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Set<Actor>> getActors() {
        Set<Actor> actors = actorService.getAllActors();
        return ResponseEntity.ok(actors);
    }

    @GetMapping("{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {
        Actor actor = actorService.getActorById(id);
        if (actor != null) {
            return ResponseEntity.ok(actor);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Actor> updateActor(
            @PathVariable Long id,
            @Valid @RequestBody ActorUpdateDTO actorUpdateDTO) {
        Actor updatedActor = actorService.updateActor(id,
                actorUpdateDTO.getName(),
                actorUpdateDTO.getBirthDate(),
                actorUpdateDTO.getMovieIds());

        if (updatedActor != null) {
            return ResponseEntity.ok(updatedActor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        boolean deleted = actorService.deleteActor(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @GetMapping(params = "name")
    public ResponseEntity<Set<Actor>> getActorsByName(@RequestParam String name) {
        Set<Actor> actors = actorService.getActorsByName(name);
        return ResponseEntity.ok(actors);
    }
}
