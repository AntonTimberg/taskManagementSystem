package com.example.taskmanagementsystem;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {
    private String username;
    private char[] password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }
}
