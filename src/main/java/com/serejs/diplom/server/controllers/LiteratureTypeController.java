package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.LiteratureType;
import com.serejs.diplom.server.repositories.LiteratureTypeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LiteratureTypeController {
    private final LiteratureTypeRepository repository;

    public LiteratureTypeController(LiteratureTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping(name = "/types")
    public List<LiteratureType> getTypes() {
        return repository.findAll();
    }
}
