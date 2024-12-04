package com.example.backend_integrador.service;

import java.time.LocalDate;
import java.util.List;
import com.example.backend_integrador.dto.ReservaDto;
import com.example.backend_integrador.enums.ReservaEstado;

public interface ReservaService {

    ReservaDto createReserva(ReservaDto reservaDto);

    ReservaDto getReservaById(Long reservaId);

    List<ReservaDto> getAllReservas();

    ReservaDto updateReserva(Long reservaId, ReservaDto updatedReserva);

    void deleteReserva(Long reservaId);

    List<ReservaDto> getAvailableReservationsForDate(LocalDate fechaReserva);

    void actualizarReservas();

    List<ReservaDto> getReservasByFechaAndEstado(LocalDate fecha, ReservaEstado estado);

}
