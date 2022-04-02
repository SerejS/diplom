package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.View;
import com.serejs.diplom.server.repositories.ViewRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ViewController {
    private final ViewRepository repository;

    public ViewController(ViewRepository repository) {
        this.repository = repository;
    }


    @GetMapping(value = "/views", produces = "application/json")
    public List<View> getViews() {
        return  repository.findAll();
    }

}
