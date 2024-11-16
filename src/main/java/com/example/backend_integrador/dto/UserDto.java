package com.example.backend_integrador.dto;

import com.example.backend_integrador.enums.UserEstado;
import com.example.backend_integrador.enums.UserRol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long userId;
    private String username;
    private String password;
    private UserEstado userEstado;
    private UserRol userRol;
    private Long employeeId; // Agrega este campo para recibir el ID del empleado
}
