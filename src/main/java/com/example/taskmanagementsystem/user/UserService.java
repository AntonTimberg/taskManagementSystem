package com.example.taskmanagementsystem.user;


import java.util.Optional;

public interface UserService {
    Optional<User> getUserByEmail(String email);
    boolean existsByEmail(String email);
}

