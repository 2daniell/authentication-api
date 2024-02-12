package com.daniel.authenticationapi.controllers;

import com.daniel.authenticationapi.models.User;
import com.daniel.authenticationapi.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    @Transactional
    public ResponseEntity<List<User>> get() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }
}
