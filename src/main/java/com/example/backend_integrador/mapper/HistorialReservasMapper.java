package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.HistorialReservasDto;
import com.example.backend_integrador.entity.HistorialReservas;
import com.example.backend_integrador.entity.Reserva;
import com.example.backend_integrador.entity.Client;

import java.time.LocalDate;
public class HistorialReservasMapper {
    public static HistorialReservasDto mapToHistorialReservasDto(HistorialReservas historialReservas) {
        HistorialReservasDto historialReservasDto = new HistorialReservasDto();
        historialReservasDto.setHistorialId(historialReservas.getHistorialId());
        historialReservasDto.setFechaCambio(historialReservas.getFechaCambio().toString());
        historialReservasDto.setEstadoFinal(historialReservas.getEstadoFinal());
        historialReservasDto.setReservaId(historialReservas.getReserva().getReservaId());
        historialReservasDto.setClientId(historialReservas.getClient().getClientId());
        return historialReservasDto;
    }
    


    public static HistorialReservas mapToHistorialReservas(HistorialReservasDto historialDto, Reserva reserva, Client client) {
        return new HistorialReservas(
            historialDto.getHistorialId(),
            LocalDate.parse(historialDto.getFechaCambio()),
            historialDto.getEstadoFinal(),
            reserva,
            client
        );
    }
    
}
