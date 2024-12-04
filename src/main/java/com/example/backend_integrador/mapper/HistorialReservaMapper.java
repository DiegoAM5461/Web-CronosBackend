package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.HistorialReservaDto;
import com.example.backend_integrador.entity.HistorialReservas;

public class HistorialReservaMapper {

    public static HistorialReservaDto toDto(HistorialReservas historialReservas) {
        return new HistorialReservaDto(
                historialReservas.getHistorialId(),
                historialReservas.getFechaCambio(),
                historialReservas.getHoraCambio(),
                historialReservas.getEstadoFinal(),
                historialReservas.getReserva() != null ? historialReservas.getReserva().getReservaId() : null,
                historialReservas.getClientId(),
                historialReservas.getReserva() != null ? historialReservas.getReserva().getFechaReserva() : null
        );
    }

    public static HistorialReservas toEntity(HistorialReservaDto dto) {
        HistorialReservas historialReservas = new HistorialReservas();
        historialReservas.setHistorialId(dto.getHistorialId());
        historialReservas.setFechaCambio(dto.getFechaCambio());
        historialReservas.setHoraCambio(dto.getHoraCambio());
        historialReservas.setEstadoFinal(dto.getEstadoFinal());
        historialReservas.setClientId(dto.getClientId());
        return historialReservas;
    }
}
