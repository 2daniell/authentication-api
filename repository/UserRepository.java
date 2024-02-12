package com.daniel.authenticationapi.repository;

import com.daniel.authenticationapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    public Optional<User> findByUsername(String name);
}
