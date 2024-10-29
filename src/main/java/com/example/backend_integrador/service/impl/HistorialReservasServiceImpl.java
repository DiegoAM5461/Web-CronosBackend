package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.HistorialReservasDto;
import com.example.backend_integrador.entity.Client;
import com.example.backend_integrador.entity.HistorialReservas;
import com.example.backend_integrador.entity.Reserva;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.mapper.HistorialReservasMapper;
import com.example.backend_integrador.repository.ClientRepository;
import com.example.backend_integrador.repository.HistorialReservasRepository;
import com.example.backend_integrador.repository.ReservaRepository;
import com.example.backend_integrador.service.HistorialReservasService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistorialReservasServiceImpl implements HistorialReservasService {

    private HistorialReservasRepository historialReservasRepository;
    private ReservaRepository reservaRepository;
    private ClientRepository clientRepository;

    @Override
    public HistorialReservasDto createHistorialReservas(HistorialReservasDto historialReservasDto) {
        // Buscar la reserva
        Reserva reserva = reservaRepository.findById(historialReservasDto.getReservaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La reserva con id: " + historialReservasDto.getReservaId() + " no existe"));

        // Buscar el cliente
        Client client = clientRepository.findById(historialReservasDto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El cliente con id: " + historialReservasDto.getClientId() + " no existe"));

        // Obtener el estado final de la reserva para el historial
        String estadoFinal = obtenerEstadoFinalDeReserva(reserva.getEstadoReserva());

        // Crear el historial con la reserva y cliente ya encontrados
        HistorialReservas historialReservas = new HistorialReservas();
        historialReservas.setFechaCambio(LocalDate.now());
        historialReservas.setEstadoFinal(estadoFinal);
        historialReservas.setReserva(reserva);
        historialReservas.setClient(client);

        // Guardar y retornar el historial
        HistorialReservas savedHistorial = historialReservasRepository.save(historialReservas);
        return HistorialReservasMapper.mapToHistorialReservasDto(savedHistorial);
    }

    private String obtenerEstadoFinalDeReserva(String estadoReserva) {
        switch (estadoReserva) {
            case "1":
                return "Pendiente";
            case "2":
                return "Confirmado";
            case "3":
                return "Cancelado";
            default:
                throw new IllegalStateException("Estado de reserva no reconocido: " + estadoReserva);
        }
    }

    @Override
    public HistorialReservasDto getHistorialReservasById(Long historialId) {
        HistorialReservas historialReservas = historialReservasRepository.findById(historialId)
                .orElseThrow(() -> new ResourceNotFoundException("El historial con id: " + historialId + " no existe"));
        return HistorialReservasMapper.mapToHistorialReservasDto(historialReservas);
    }

    @Override
    public List<HistorialReservasDto> getAllHistorialReservas() {
        List<HistorialReservas> historiales = historialReservasRepository.findAll();
        return historiales.stream().map(HistorialReservasMapper::mapToHistorialReservasDto)
                .collect(Collectors.toList());
    }
}
