package com.example.backend_integrador.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_integrador.dto.ReservaDto;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.service.ReservaService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = {
    "http://localhost:3000", 
    "https://frontend-cronos.vercel.app"
})
@AllArgsConstructor
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    private ReservaService reservaService;

    // Create a new reserva REST API
    @PostMapping
    public ResponseEntity<ReservaDto> createReserva(@RequestBody ReservaDto reservaDto) {
        logger.info("POST /api/reservas - Creando nueva reserva");
        logger.debug("Datos de la reserva a crear: {}", reservaDto);
        try {
            ReservaDto savedReserva = reservaService.createReserva(reservaDto);
            logger.info("Reserva creada con éxito: {}", savedReserva);
            return new ResponseEntity<>(savedReserva, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            logger.error("Error al crear la reserva: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get a reserva by ID REST API
    @GetMapping("/{reservaId}")
    public ResponseEntity<ReservaDto> getReservaById(@PathVariable("reservaId") Long reservaId) {
        logger.info("GET /api/reservas/{} - Obtener reserva por ID", reservaId);
        try {
            ReservaDto reservaDto = reservaService.getReservaById(reservaId);
            logger.debug("Reserva encontrada: {}", reservaDto);
            return ResponseEntity.ok(reservaDto);
        } catch (ResourceNotFoundException e) {
            logger.warn("No se encontró reserva con ID: {}", reservaId);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get all reservas REST API
    @GetMapping
    public ResponseEntity<List<ReservaDto>> getAllReservas() {
        logger.info("GET /api/reservas - Obtener todas las reservas");
        List<ReservaDto> reservas = reservaService.getAllReservas();
        logger.debug("Cantidad de reservas encontradas: {}", reservas.size());
        return ResponseEntity.ok(reservas);
    }

    // Update a reserva by ID REST API
    @PutMapping("/{reservaId}")
    public ResponseEntity<ReservaDto> updateReserva(@PathVariable("reservaId") Long reservaId,
                                                    @RequestBody ReservaDto updatedReserva) {
        logger.info("PUT /api/reservas/{} - Actualizando reserva", reservaId);
        logger.debug("Datos para actualizar: {}", updatedReserva);
        try {
            ReservaDto reservaDto = reservaService.updateReserva(reservaId, updatedReserva);
            logger.info("Reserva actualizada con éxito: {}", reservaDto);
            return ResponseEntity.ok(reservaDto);
        } catch (ResourceNotFoundException e) {
            logger.error("Error al actualizar reserva con ID {}: {}", reservaId, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Delete a reserva by ID REST API
    @DeleteMapping("/{reservaId}")
    public ResponseEntity<String> deleteReserva(@PathVariable("reservaId") Long reservaId) {
        logger.info("DELETE /api/reservas/{} - Eliminando reserva", reservaId);
        try {
            reservaService.deleteReserva(reservaId);
            logger.info("Reserva con ID {} eliminada correctamente", reservaId);
            return ResponseEntity.ok("Reserva eliminada correctamente");
        } catch (ResourceNotFoundException e) {
            logger.error("Error al eliminar la reserva con ID {}: {}", reservaId, e.getMessage());
            return new ResponseEntity<>("Error al eliminar la reserva", HttpStatus.NOT_FOUND);
        }
    }

    // Get available reservations by date
    @GetMapping("/disponibles")
    public List<ReservaDto> getAvailableReservations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReserva) {
        logger.info("GET /api/reservas/disponibles - Consultando reservas disponibles para la fecha: {}", fechaReserva);
        List<ReservaDto> disponibles = reservaService.getAvailableReservationsForDate(fechaReserva);
        logger.debug("Cantidad de reservas disponibles: {}", disponibles.size());
        return disponibles;
    }

    // Update reservation states
    @PutMapping("/estado/actualizar")
    public ResponseEntity<String> actualizarEstados() {
        logger.info("PUT /api/reservas/estado/actualizar - Actualizando estados de reservas");
        reservaService.actualizarReservas();
        logger.info("Estados de reservas actualizados correctamente");
        return ResponseEntity.ok("Reservas actualizadas correctamente.");
    }
}
