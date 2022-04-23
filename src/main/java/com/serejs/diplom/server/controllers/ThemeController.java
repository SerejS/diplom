package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.Theme;
import com.serejs.diplom.server.repositories.ThemeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ThemeController {
    private final ThemeRepository repository;

    public ThemeController(ThemeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/themes")
    public List<Theme> getThemes(@RequestParam Long projectId) {
        return repository.findThemesByProjectId(projectId);
    }

}
