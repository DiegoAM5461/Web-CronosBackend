package com.example.backend_integrador.service;

import java.util.List;
import com.example.backend_integrador.dto.ReservaDto;

public interface ReservaService {
    
    ReservaDto createReserva(ReservaDto reservaDto);

    ReservaDto getReservaById(Long reservaId);

    List<ReservaDto> getAllReservas();

    ReservaDto updateReserva(Long reservaId, ReservaDto updatedReserva);

    void deleteReserva(Long reservaId);
}
