package com.example.backend_integrador.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("securePassword123");
        System.out.println(encodedPassword); // Usa este valor en la base de datos
    }
}

