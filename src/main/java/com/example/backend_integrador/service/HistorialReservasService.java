package com.example.backend_integrador.service;

import com.example.backend_integrador.dto.HistorialReservaDto;
import java.util.List;

public interface HistorialReservasService {
    
    // Crear un nuevo historial de reserva
    HistorialReservaDto createHistorialReservas(HistorialReservaDto historialReservasDto);
    
    // Obtener historial de reserva por su ID
    HistorialReservaDto getHistorialReservasById(Long historialId);
    
    // Obtener todos los historiales de reservas
    List<HistorialReservaDto> getAllHistorialReservas();
    
    // Filtrar historial por clienteId
    List<HistorialReservaDto> filtrarPorClienteId(Long clienteId);
    
    // Filtrar historial por nombreCliente
    List<HistorialReservaDto> filtrarPorNombreCliente(String nombreCliente);
    
    // Filtrar historial por reservaId
    List<HistorialReservaDto> filtrarPorReservaId(Long reservaId);
    
    // Filtrar historial por fechaReserva
    List<HistorialReservaDto> filtrarPorFechaReserva(java.time.LocalDate fechaReserva);
    
    // Filtrar historial por estadoReserva
    List<HistorialReservaDto> filtrarPorEstadoReserva(String estadoReserva);
}
