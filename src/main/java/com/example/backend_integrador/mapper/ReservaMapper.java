package com.example.backend_integrador.mapper;

import com.example.backend_integrador.entity.Client;
import com.example.backend_integrador.dto.ReservaDto;
import com.example.backend_integrador.entity.Reserva;
import com.example.backend_integrador.entity.BoxCronos;
public class ReservaMapper {
    public static ReservaDto mapToReservaDto(Reserva reserva) {
        ReservaDto reservaDto = new ReservaDto();
        reservaDto.setReservaId(reserva.getReservaId());
        reservaDto.setFechaReserva(reserva.getFechaReserva());
        reservaDto.setHoraInicio(reserva.getHoraInicio());
        reservaDto.setHoraFin(reserva.getHoraFin());
        reservaDto.setEstadoReserva(reserva.getEstadoReserva());
        reservaDto.setClientId(reserva.getClient().getClientId());
        reservaDto.setBoxId(reserva.getBox().getBoxId());
        reservaDto.setPrimerNombre(reserva.getClient().getPrimerNombre());
        reservaDto.setPrimerApellido(reserva.getClient().getPrimerApellido());
        reservaDto.setEmail(reserva.getClient().getEmail());
        reservaDto.setTelefono(reserva.getClient().getTelefono());
        return reservaDto;
    }

    public static Reserva mapToReserva(ReservaDto reservaDto, Client client, BoxCronos box) {
        Reserva reserva = new Reserva();
        reserva.setReservaId(reservaDto.getReservaId());
        reserva.setFechaReserva(reservaDto.getFechaReserva());
        reserva.setHoraInicio(reservaDto.getHoraInicio());
        reserva.setHoraFin(reservaDto.getHoraFin());
        reserva.setEstadoReserva(reservaDto.getEstadoReserva());
        reserva.setClient(client);
        reserva.setBox(box);
        return reserva;
    }
}
