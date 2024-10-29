package com.example.backend_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialReservasDto {
    private Long historialId;
    private String fechaCambio;
    private String estadoFinal;
    private Long reservaId;
    private Long clientId;
}
