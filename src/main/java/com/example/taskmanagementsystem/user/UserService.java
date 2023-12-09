package com.example.taskmanagementsystem.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> getUserByEmail(String email);
    public boolean existsByEmail(String email);
}

