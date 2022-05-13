package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.Literature;
import com.serejs.diplom.server.entities.Theme;
import com.serejs.diplom.server.repositories.LiteratureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/literatures")
public class LiteratureController {
    private final LiteratureRepository repository;

    public LiteratureController(LiteratureRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = "application/json")
    public List<Literature> getLiteratures(@RequestParam Long projectId) {
        return repository.findLiteraturesByProjectId(projectId);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Long> createLiteratures(@RequestBody Literature literature) {
        var newLiterature= repository.save(literature);

        return new ResponseEntity<>(newLiterature.getId(), HttpStatus.CREATED);
    }
}
