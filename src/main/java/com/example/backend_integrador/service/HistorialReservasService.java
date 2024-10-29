package com.example.backend_integrador.service;

import com.example.backend_integrador.dto.HistorialReservasDto;
import java.util.List;

public interface HistorialReservasService {
    HistorialReservasDto createHistorialReservas(HistorialReservasDto historialReservasDto);
    HistorialReservasDto getHistorialReservasById(Long historialId);
    List<HistorialReservasDto> getAllHistorialReservas();
}