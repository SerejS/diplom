package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.LiteratureType;
import com.serejs.diplom.server.repositories.LiteratureTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
public class LiteratureTypeController {
    private final LiteratureTypeRepository repository;

    public LiteratureTypeController(LiteratureTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<LiteratureType> getTypes(@RequestParam Long viewId) {
        return repository.findLiteratureTypeByViewId(viewId);
    }

    @PostMapping
    public ResponseEntity<LiteratureType> createType(@RequestBody LiteratureType type) {
        repository.save(type);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
