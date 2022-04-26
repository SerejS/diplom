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
    public ResponseEntity<Long> createType(@RequestBody LiteratureType type) {
        var newType = repository.save(type);

        return new ResponseEntity<>(newType.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LiteratureType> deleteType(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
