package com.movies.movies_api.controller;

import com.movies.movies_api.entity.Actor;
import com.movies.movies_api.service.ActorService;
import com.movies.movies_api.dto.ActorUpdateDTO;
import com.movies.movies_api.exception.ResourceNotFoundException;  // import the custom exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<?> createActor(@Valid @RequestBody Actor actor) {
        try {
            actorService.addActor(actor);
            return new ResponseEntity<>(actor, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getActors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Actor> actorsPage = actorService.getAllActors(page, size);

        // Return the paginated response
        return ResponseEntity.ok(actorsPage);
    }


    @GetMapping("{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {
        Actor actor = actorService.getActorById(id);
        if (actor == null) {
            throw new ResourceNotFoundException("Actor not found with id " + id, HttpStatus.NOT_FOUND.toString());
        }
        return ResponseEntity.ok(actor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Actor> updateActor(
            @PathVariable Long id,
            @Valid @RequestBody ActorUpdateDTO actorUpdateDTO) {
        Actor updatedActor = actorService.updateActor(id,
                actorUpdateDTO.getName(),
                actorUpdateDTO.getBirthDate(),
                actorUpdateDTO.getMovieIds());

        if (updatedActor == null) {
            throw new ResourceNotFoundException("Actor not found with id " + id, HttpStatus.NOT_FOUND.toString());
        }
        return ResponseEntity.ok(updatedActor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActor(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        try {
            boolean deleted = actorService.deleteActor(id, force);
            if (!deleted) {
                throw new ResourceNotFoundException("Actor not found with id " + id, HttpStatus.NOT_FOUND.toString());
            }
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalStateException e) {
            // Handle the case where actor can't be deleted due to associated movies
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }




    @GetMapping(params = "name")
    public ResponseEntity<Set<Actor>> getActorsByName(@RequestParam String name) {
        Set<Actor> actors = actorService.getActorsByName(name);
        return ResponseEntity.ok(actors);
    }
}
