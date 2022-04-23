package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.LiteratureType;
import com.serejs.diplom.server.repositories.LiteratureTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LiteratureTypeController {
    private final LiteratureTypeRepository repository;

    public LiteratureTypeController(LiteratureTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/types")
    public List<LiteratureType> getTypes(@RequestParam Long viewId) {
        return repository.findLiteratureTypeByViewId(viewId);
    }

    @PostMapping(value = "/types")
    public void addType(@RequestBody LiteratureType type) {
        repository.save(type);
    }
}
