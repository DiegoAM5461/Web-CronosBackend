package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.HistorialReservaDto;
import com.example.backend_integrador.entity.HistorialReservas;
import com.example.backend_integrador.mapper.HistorialReservaMapper;
import com.example.backend_integrador.repository.HistorialReservasRepository;
import com.example.backend_integrador.service.HistorialReservasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialReservasServiceImpl implements HistorialReservasService {

    @Autowired
    private HistorialReservasRepository historialReservasRepository;

    @Override
    public HistorialReservaDto createHistorialReservas(HistorialReservaDto historialReservasDto) {
        HistorialReservas historialReservas = HistorialReservaMapper.toEntity(historialReservasDto);
        historialReservas.setFechaCambio(LocalDate.now());
        historialReservas.setHoraCambio(java.time.LocalTime.now());
        HistorialReservas savedHistorial = historialReservasRepository.save(historialReservas);
        return HistorialReservaMapper.toDto(savedHistorial);
    }

    @Override
    public HistorialReservaDto getHistorialReservasById(Long historialId) {
        HistorialReservas historialReservas = historialReservasRepository.findById(historialId)
                .orElseThrow(() -> new RuntimeException("HistorialReserva not found"));
        return HistorialReservaMapper.toDto(historialReservas);
    }

    @Override
    public List<HistorialReservaDto> getAllHistorialReservas() {
        return historialReservasRepository.findAll().stream()
                .map(HistorialReservaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialReservaDto> filtrarPorClienteId(Long clienteId) {
        return historialReservasRepository.findAll().stream()
                .filter(historial -> historial.getClientId() != null && historial.getClientId().equals(clienteId))
                .map(HistorialReservaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialReservaDto> filtrarPorNombreCliente(String nombreCliente) {
        return historialReservasRepository.findAll().stream()
                .filter(historial -> historial.getReserva().getClient().getPrimerNombre().equalsIgnoreCase(nombreCliente))
                .map(HistorialReservaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialReservaDto> filtrarPorReservaId(Long reservaId) {
        return historialReservasRepository.findAll().stream()
                .filter(historial -> historial.getReserva().getReservaId().equals(reservaId))
                .map(HistorialReservaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialReservaDto> filtrarPorFechaReserva(LocalDate fechaReserva) {
        return historialReservasRepository.findAll().stream()
                .filter(historial -> historial.getReserva().getFechaReserva().equals(fechaReserva))
                .map(HistorialReservaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialReservaDto> filtrarPorEstadoReserva(String estadoReserva) {
        return historialReservasRepository.findAll().stream()
                .filter(historial -> historial.getEstadoFinal().name().equalsIgnoreCase(estadoReserva))
                .map(HistorialReservaMapper::toDto)
                .collect(Collectors.toList());
    }
}
