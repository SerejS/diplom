package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.entities.User;
import com.serejs.diplom.server.repositories.UserRepository;
import com.serejs.diplom.server.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public void register(@RequestParam String username, @RequestParam String password) {
        var user = new User();
        if (username.isEmpty() || password.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (repository.findByUsername(username) != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT);

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        repository.save(user);
    }

    //Проверка авторизации
    @PostMapping("/auth")
    public ResponseEntity<User> auth(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok().build();
    }
}
