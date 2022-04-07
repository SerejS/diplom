package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.Project;
import com.serejs.diplom.server.repositories.ProjectRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectController {
    private final ProjectRepository repository;

    public ProjectController(ProjectRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/projects", produces = "application/json")
    public List<Project> getProjects(@RequestParam Long viewId) {
        return repository.findProjectByViewId(viewId);
    }
}
