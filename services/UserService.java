package com.daniel.authenticationapi.services;

import com.daniel.authenticationapi.models.User;
import com.daniel.authenticationapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User find(String name) {
        return repository.findByUsername(name).orElse(null);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User save(User user) {
        return repository.save(user);
    }

}
