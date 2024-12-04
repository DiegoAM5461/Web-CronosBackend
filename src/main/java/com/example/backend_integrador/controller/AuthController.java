package com.example.backend_integrador.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend_integrador.entity.SpringSc.JwtResponse;
import com.example.backend_integrador.entity.Login.LoginRequest;
import com.example.backend_integrador.entity.Login.RegisterRequest;
import com.example.backend_integrador.service.auth.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    // Endpoint para login, devuelve el token en la respuesta
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        logger.info("POST /auth/login - Intento de inicio de sesión para usuario: {}", request.getUsername());
        try {
            JwtResponse response = authService.login(request);
            logger.info("Inicio de sesión exitoso para usuario: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error en el inicio de sesión para usuario: {} - Error: {}", request.getUsername(), e.getMessage());
            throw e;
        }
    }

    // Endpoint para registro, devuelve el token en la respuesta
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest request) {
        logger.info("POST /auth/register - Intento de registro para usuario: {}", request.getUsername());
        try {
            JwtResponse response = authService.register(request);
            logger.info("Registro exitoso para usuario: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error en el registro para usuario: {} - Error: {}", request.getUsername(), e.getMessage());
            throw e;
        }
    }
}
