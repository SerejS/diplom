package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.Theme;
import com.serejs.diplom.server.repositories.ThemeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {
    private final ThemeRepository repository;

    public ThemeController(ThemeRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = "application/json")
    public List<Theme> getThemes(@RequestParam Long projectId) {
        return repository.findThemesByProjectId(projectId);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<List<Theme>> createThemes(@RequestBody List<Theme> themes) {
        repository.saveAll(themes);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
