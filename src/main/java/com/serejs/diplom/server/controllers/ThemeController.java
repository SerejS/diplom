package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.Theme;
import com.serejs.diplom.server.repositories.ThemeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ThemeController {
    private final ThemeRepository repository;

    public ThemeController(ThemeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/themes")
    public List<Theme> getThemes() {
        return repository.findAll();
    }

}
