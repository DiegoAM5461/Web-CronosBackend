package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.HistorialReservasDto;
import com.example.backend_integrador.entity.Client;
import com.example.backend_integrador.entity.HistorialReservas;
import com.example.backend_integrador.entity.Reserva;
import com.example.backend_integrador.enums.ReservaEstado;
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
        Reserva reserva = buscarReserva(historialReservasDto.getReservaId());
        Client client = buscarCliente(historialReservasDto.getClientId());
        String estadoFinal = obtenerEstadoFinalDeReserva(reserva.getEstadoReserva());

        HistorialReservas historialReservas = construirHistorialReservas(reserva, client, estadoFinal);
        HistorialReservas savedHistorial = historialReservasRepository.save(historialReservas);

        return HistorialReservasMapper.mapToHistorialReservasDto(savedHistorial);
    }

    private Reserva buscarReserva(Long reservaId) {
        return reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La reserva con id: " + reservaId + " no existe"));
    }

    private Client buscarCliente(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El cliente con id: " + clientId + " no existe"));
    }

    private HistorialReservas construirHistorialReservas(Reserva reserva, Client client, String estadoFinal) {
        HistorialReservas historialReservas = new HistorialReservas();
        historialReservas.setFechaCambio(LocalDate.now());
        historialReservas.setEstadoFinal(estadoFinal);
        historialReservas.setReserva(reserva);
        historialReservas.setClient(client);
        return historialReservas;
    }

    private String obtenerEstadoFinalDeReserva(ReservaEstado estadoReserva) {
        switch (estadoReserva) {
            case PENDIENTE:
                return "Pendiente";
            case CONFIRMADA:
                return "Confirmada";
            case CANCELADA:
                return "Cancelada";
            default:
                throw new IllegalStateException("Estado de reserva no reconocido: " + estadoReserva);
        }
    }

    @Override
    public HistorialReservasDto getHistorialReservasById(Long historialId) {
        HistorialReservas historialReservas = historialReservasRepository.findById(historialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El historial con id: " + historialId + " no existe"));
        return HistorialReservasMapper.mapToHistorialReservasDto(historialReservas);
    }

    @Override
    public List<HistorialReservasDto> getAllHistorialReservas() {
        List<HistorialReservas> historialReservasList = historialReservasRepository.findAll();
        return historialReservasList.stream()
                .map(HistorialReservasMapper::mapToHistorialReservasDto)
                .collect(Collectors.toList());
    }
}
