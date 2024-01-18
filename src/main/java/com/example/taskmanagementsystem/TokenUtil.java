package com.example.taskmanagementsystem;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenUtil {
    String generateToken(UserDetails userDetails);
    String getUsernameFromToken(String token);
    Boolean validateToken(String token, UserDetails userDetails);
}
