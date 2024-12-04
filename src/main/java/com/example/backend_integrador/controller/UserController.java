package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.UserDto;
import com.example.backend_integrador.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        logger.info("GET /api/users/{} - Obtener usuario por ID", id);
        UserDto user = userService.getUserById(id);
        if (user != null) {
            logger.debug("Usuario encontrado: {}", user);
        } else {
            logger.warn("No se encontró un usuario con ID: {}", id);
        }
        return user;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        logger.info("GET /api/users - Obtener todos los usuarios");
        List<UserDto> users = userService.getAllUsers();
        logger.debug("Cantidad de usuarios encontrados: {}", users.size());
        return users;
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        logger.info("POST /api/users - Crear un nuevo usuario");
        logger.debug("Datos del usuario a crear: {}", userDto);
        UserDto createdUser = userService.createUser(userDto);
        logger.info("Usuario creado con éxito: {}", createdUser);
        return createdUser;
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        logger.info("PUT /api/users/{} - Actualizar usuario", id);
        logger.debug("Datos para actualizar: {}", userDto);
        UserDto updatedUser = userService.updateUser(id, userDto);
        logger.info("Usuario actualizado con éxito: {}", updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        logger.info("DELETE /api/users/{} - Eliminar usuario", id);
        try {
            userService.deleteUser(id);
            logger.info("Usuario con ID {} eliminado con éxito", id);
        } catch (Exception e) {
            logger.error("Error al eliminar el usuario con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}
