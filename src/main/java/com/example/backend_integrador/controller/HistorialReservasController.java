package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.HistorialReservaDto;
import com.example.backend_integrador.service.HistorialReservasService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/historial-reservas")
public class HistorialReservasController {

    private final HistorialReservasService historialReservasService;

    // Crear un nuevo historial de reserva
    @PostMapping
    public ResponseEntity<HistorialReservaDto> createHistorialReservas(
            @RequestBody HistorialReservaDto historialReservasDto) {
        HistorialReservaDto savedHistorial = historialReservasService.createHistorialReservas(historialReservasDto);
        return new ResponseEntity<>(savedHistorial, HttpStatus.CREATED);
    }

    // Obtener historial de reserva por su ID
    @GetMapping("/{historialId}")
    public ResponseEntity<HistorialReservaDto> getHistorialReservasById(@PathVariable Long historialId) {
        HistorialReservaDto historialReservasDto = historialReservasService.getHistorialReservasById(historialId);
        return ResponseEntity.ok(historialReservasDto);
    }

    // Obtener todos los historiales de reservas
    @GetMapping
    public ResponseEntity<List<HistorialReservaDto>> getAllHistorialReservas() {
        List<HistorialReservaDto> historialReservasList = historialReservasService.getAllHistorialReservas();
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por clienteId
    @GetMapping("/filtrar/clienteId/{clienteId}")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorClienteId(@PathVariable Long clienteId) {
        List<HistorialReservaDto> historialReservasList = historialReservasService.filtrarPorClienteId(clienteId);
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por nombreCliente
    @GetMapping("/filtrar/nombreCliente/{nombreCliente}")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorNombreCliente(@PathVariable String nombreCliente) {
        List<HistorialReservaDto> historialReservasList = historialReservasService
                .filtrarPorNombreCliente(nombreCliente);
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por reservaId
    @GetMapping("/filtrar/reservaId/{reservaId}")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorReservaId(@PathVariable Long reservaId) {
        List<HistorialReservaDto> historialReservasList = historialReservasService.filtrarPorReservaId(reservaId);
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por fechaReserva
    @GetMapping("/filtrar/fechaReserva")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorFechaReserva(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReserva) {
        List<HistorialReservaDto> historialReservasList = historialReservasService.filtrarPorFechaReserva(fechaReserva);
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por estadoReserva
    @GetMapping("/filtrar/estadoReserva/{estadoReserva}")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorEstadoReserva(@PathVariable String estadoReserva) {
        List<HistorialReservaDto> historialReservasList = historialReservasService
                .filtrarPorEstadoReserva(estadoReserva);
        return ResponseEntity.ok(historialReservasList);
    }
}
