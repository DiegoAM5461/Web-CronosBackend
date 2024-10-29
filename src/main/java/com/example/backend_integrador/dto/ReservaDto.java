package com.example.backend_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDto {
    private Long reservaId;
    private String fechaReserva;
    private String horaInicio;
    private String horaFin;
    private String estadoReserva;
    
    private Long clientId;
    private Long boxId;
    private String primerNombre;
    private String primerApellido;
    private String email;
    private String telefono;

    private String disponibilidad; // Nuevo campo para almacenar la disponibilidad del box
}