package com.example.backend_integrador.dto;

import com.example.backend_integrador.enums.ReservaEstado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialReservaDto {

    private Long historialId;
    private LocalDate fechaCambio;
    private LocalTime horaCambio;
    private ReservaEstado estadoFinal;
    private Long reservaId;
    private Long clientId;  // Nuevo campo
}
