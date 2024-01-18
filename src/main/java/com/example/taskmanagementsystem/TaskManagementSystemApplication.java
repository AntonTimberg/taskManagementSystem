package com.example.taskmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class TaskManagementSystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TaskManagementSystemApplication.class, args);
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        String hashedPassword = encoder.encode("user");
        System.out.println("Hashed password: " + hashedPassword);
    }

}
