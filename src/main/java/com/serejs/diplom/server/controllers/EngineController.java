package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.EngineParams;
import com.serejs.diplom.server.repositories.EngineParamsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/engines")
public class EngineController {
    private final EngineParamsRepository repository;

    public EngineController(EngineParamsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<EngineParams> getEngines(@RequestParam Long projectId) {
        return repository.findEngineParamsByProjectID(projectId);
    }

    @PostMapping
    public ResponseEntity<List<EngineParams>> addEngines(@RequestBody List<EngineParams> ep) {
        repository.saveAll(ep);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
