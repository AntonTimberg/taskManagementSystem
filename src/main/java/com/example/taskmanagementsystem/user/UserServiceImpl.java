package com.example.taskmanagementsystem.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepository;

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
