package com.daniel.authenticationapi.controllers;

import com.daniel.authenticationapi.models.User;
import com.daniel.authenticationapi.objects.dto.AuthenticationDTO;
import com.daniel.authenticationapi.objects.dto.ResponseDTO;
import com.daniel.authenticationapi.services.AuthenticationService;
import com.daniel.authenticationapi.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private UserService service;
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<Object> signup(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        if (service.find(authenticationDTO.username()) != null) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        if (authenticationDTO.password().length() < 4) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        var user = new User(authenticationDTO.username(), new BCryptPasswordEncoder().encode(authenticationDTO.password()));
        service.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(user.getId(), user.getUsername(), user.getCreated()));
    }

    @PostMapping("/signin")
    @Transactional
    public ResponseEntity<Object> signin(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        if (service.find(authenticationDTO.username()) == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        var user = service.find(authenticationDTO.username()); var hash = new BCryptPasswordEncoder();
        if (!hash.matches(authenticationDTO.password(), user.getPassword())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        var authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(authenticationToken));
    }

}
