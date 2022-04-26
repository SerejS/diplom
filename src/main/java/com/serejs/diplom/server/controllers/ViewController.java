package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.View;
import com.serejs.diplom.server.repositories.UserRepository;
import com.serejs.diplom.server.repositories.ViewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/views")
public class ViewController {
    private final UserRepository userRepo;
    private final ViewRepository viewRepo;

    public ViewController(UserRepository userRepo, ViewRepository viewRepo) {
        this.userRepo = userRepo;
        this.viewRepo = viewRepo;
    }

    @GetMapping(produces = "application/json")
    public List<View> getViews() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails userDetail)) return new ArrayList<>();

        var user = userRepo.findByUsername(userDetail.getUsername());
        return viewRepo.findAllByUser(user);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Long> createView(@RequestBody View view) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails userDetail))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        var user = userRepo.findByUsername(userDetail.getUsername());
        view.setUser(user);

        var id = viewRepo.save(view).getId();
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<View> deleteView(@PathVariable Long id) {
        if (!viewRepo.existsById(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        viewRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
