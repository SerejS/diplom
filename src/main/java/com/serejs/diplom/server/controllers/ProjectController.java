package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.Project;
import com.serejs.diplom.server.repositories.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectController {
    private final ProjectRepository repository;

    public ProjectController(ProjectRepository repository) {
        this.repository = repository;
    }

    //Получение всех проектов
    @GetMapping(value = "/projects", produces = "application/json")
    public List<Project> getProjects(@RequestParam Long viewId) {
        return repository.findProjectByViewId(viewId);
    }

    //Создание нового проекта
    @PostMapping(value = "/project", produces = "application/json")
    public ResponseEntity<Long> createProject(@RequestBody Project project) {
        var projectID = repository.save(project).getId();

        return new ResponseEntity<>(projectID, HttpStatus.CREATED);
    }

    //Получение данных конкретного проекта
    @GetMapping(value = "/project/{id}", produces = "application/json")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        var project = repository.findById(id);

        if (project.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(project.get());
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
