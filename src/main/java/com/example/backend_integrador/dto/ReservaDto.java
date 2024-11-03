package com.example.backend_integrador.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.backend_integrador.enums.ReservaEstado;

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
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private ReservaEstado estadoReserva;
    private Long clientId;
    private Long boxId;
    private String primerNombre;
    private String primerApellido;
    private String email;
    private String telefono;
    private Integer boxCapacidad;  // Nuevo campo
}
