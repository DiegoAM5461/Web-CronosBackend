package com.example.backend_integrador.entity.Login;

import com.example.backend_integrador.entity.Employee;
import com.example.backend_integrador.enums.UserEstado;
import com.example.backend_integrador.enums.UserRol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    String username;
    String password;
    UserEstado userEstado;
    UserRol userRol;
    Employee employee;
}