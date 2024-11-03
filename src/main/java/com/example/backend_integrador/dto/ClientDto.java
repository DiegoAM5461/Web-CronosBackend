package com.example.backend_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long clientId;
    private String primerNombre;
    private String primerApellido;
    private String segundoApellido;
    private String email;
    private String telefono;
}