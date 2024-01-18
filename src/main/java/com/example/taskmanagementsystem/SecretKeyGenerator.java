package com.example.taskmanagementsystem;

import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Component
public class SecretKeyGenerator {

    private final SecretKey secretKey;

    public SecretKeyGenerator() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(256);
        this.secretKey = keyGenerator.generateKey();
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}
