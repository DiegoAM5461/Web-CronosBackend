package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.ReservaDto;
import com.example.backend_integrador.entity.Reserva;
import com.example.backend_integrador.entity.Client;
import com.example.backend_integrador.entity.BoxCronos;

public class ReservaMapper {
    public static Reserva mapToReserva(ReservaDto reservaDto, Client client, BoxCronos box) {
        Reserva reserva = new Reserva(
            reservaDto.getReservaId(),
            reservaDto.getFechaReserva(),
            reservaDto.getHoraInicio(),
            reservaDto.getHoraFin(),
            reservaDto.getEstadoReserva(),
            client,
            box,
            box.getBoxEstado() // Setear el estado de disponibilidad directamente desde box
        );
        return reserva;
    }

    public static ReservaDto mapToReservaDto(Reserva reserva) {
        return new ReservaDto(
            reserva.getReservaId(),
            reserva.getFechaReserva(),
            reserva.getHoraInicio(),
            reserva.getHoraFin(),
            reserva.getEstadoReserva(),
            reserva.getClient().getClientId(),
            reserva.getBox().getBoxId(),
            reserva.getClient().getPrimerNombre(),
            reserva.getClient().getPrimerApellido(),
            reserva.getClient().getEmail(),
            reserva.getClient().getTelefono(),
            reserva.getDisponibilidad()
        );
    }
}

