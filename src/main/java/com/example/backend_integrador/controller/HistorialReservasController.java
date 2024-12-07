package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.HistorialReservaDto;
import com.example.backend_integrador.service.HistorialReservasService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {
    "http://localhost:3000", 
    "https://frontend-cronos.vercel.app"
})
@AllArgsConstructor
@RestController
@RequestMapping("/api/historial-reservas")
public class HistorialReservasController {

    private static final Logger logger = LoggerFactory.getLogger(HistorialReservasController.class);

    private final HistorialReservasService historialReservasService;

    // Crear un nuevo historial de reserva
    @PostMapping
    public ResponseEntity<HistorialReservaDto> createHistorialReservas(
            @RequestBody HistorialReservaDto historialReservasDto) {
        logger.info("POST /api/historial-reservas - Creando un nuevo historial de reserva");
        logger.debug("Datos del historial a crear: {}", historialReservasDto);
        HistorialReservaDto savedHistorial = historialReservasService.createHistorialReservas(historialReservasDto);
        logger.info("Historial de reserva creado con éxito: {}", savedHistorial);
        return new ResponseEntity<>(savedHistorial, HttpStatus.CREATED);
    }

    // Obtener historial de reserva por su ID
    @GetMapping("/{historialId}")
    public ResponseEntity<HistorialReservaDto> getHistorialReservasById(@PathVariable Long historialId) {
        logger.info("GET /api/historial-reservas/{} - Obtener historial de reserva por ID", historialId);
        HistorialReservaDto historialReservasDto = historialReservasService.getHistorialReservasById(historialId);
        if (historialReservasDto != null) {
            logger.debug("Historial de reserva encontrado: {}", historialReservasDto);
        } else {
            logger.warn("No se encontró un historial de reserva con ID: {}", historialId);
        }
        return ResponseEntity.ok(historialReservasDto);
    }

    // Obtener todos los historiales de reservas
    @GetMapping
    public ResponseEntity<List<HistorialReservaDto>> getAllHistorialReservas() {
        logger.info("GET /api/historial-reservas - Obtener todos los historiales de reservas");
        List<HistorialReservaDto> historialReservasList = historialReservasService.getAllHistorialReservas();
        logger.debug("Cantidad de historiales encontrados: {}", historialReservasList.size());
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por clienteId
    @GetMapping("/filtrar/clienteId/{clienteId}")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorClienteId(@PathVariable Long clienteId) {
        logger.info("GET /api/historial-reservas/filtrar/clienteId/{} - Filtrar historial por clienteId", clienteId);
        List<HistorialReservaDto> historialReservasList = historialReservasService.filtrarPorClienteId(clienteId);
        logger.debug("Cantidad de historiales encontrados para clienteId {}: {}", clienteId, historialReservasList.size());
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por nombreCliente
    @GetMapping("/filtrar/nombreCliente/{nombreCliente}")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorNombreCliente(@PathVariable String nombreCliente) {
        logger.info("GET /api/historial-reservas/filtrar/nombreCliente/{} - Filtrar historial por nombreCliente", nombreCliente);
        List<HistorialReservaDto> historialReservasList = historialReservasService.filtrarPorNombreCliente(nombreCliente);
        logger.debug("Cantidad de historiales encontrados para nombreCliente {}: {}", nombreCliente, historialReservasList.size());
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por reservaId
    @GetMapping("/filtrar/reservaId/{reservaId}")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorReservaId(@PathVariable Long reservaId) {
        logger.info("GET /api/historial-reservas/filtrar/reservaId/{} - Filtrar historial por reservaId", reservaId);
        List<HistorialReservaDto> historialReservasList = historialReservasService.filtrarPorReservaId(reservaId);
        logger.debug("Cantidad de historiales encontrados para reservaId {}: {}", reservaId, historialReservasList.size());
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por fechaReserva
    @GetMapping("/filtrar/fechaReserva")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorFechaReserva(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReserva) {
        logger.info("GET /api/historial-reservas/filtrar/fechaReserva - Filtrar historial por fechaReserva {}", fechaReserva);
        List<HistorialReservaDto> historialReservasList = historialReservasService.filtrarPorFechaReserva(fechaReserva);
        logger.debug("Cantidad de historiales encontrados para fechaReserva {}: {}", fechaReserva, historialReservasList.size());
        return ResponseEntity.ok(historialReservasList);
    }

    // Filtrar historial por estadoReserva
    @GetMapping("/filtrar/estadoReserva/{estadoReserva}")
    public ResponseEntity<List<HistorialReservaDto>> filtrarPorEstadoReserva(@PathVariable String estadoReserva) {
        logger.info("GET /api/historial-reservas/filtrar/estadoReserva/{} - Filtrar historial por estadoReserva", estadoReserva);
        List<HistorialReservaDto> historialReservasList = historialReservasService.filtrarPorEstadoReserva(estadoReserva);
        logger.debug("Cantidad de historiales encontrados para estadoReserva {}: {}", estadoReserva, historialReservasList.size());
        return ResponseEntity.ok(historialReservasList);
    }
}
