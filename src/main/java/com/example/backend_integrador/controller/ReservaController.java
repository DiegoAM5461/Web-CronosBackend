package com.example.backend_integrador.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.example.backend_integrador.service.ReservaService;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    
    @Autowired
    private ReservaService reservaService;

    // Create a new reserva REST API
    @PostMapping
    public ResponseEntity<ReservaDto> createReserva(@RequestBody ReservaDto reservaDto) {
        try {
            ReservaDto savedReserva = reservaService.createReserva(reservaDto);
            return new ResponseEntity<>(savedReserva, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get a reserva by ID REST API
    @GetMapping("/{reservaId}")
    public ResponseEntity<ReservaDto> getReservaById(@PathVariable("reservaId") Long reservaId) {
        ReservaDto reservaDto = reservaService.getReservaById(reservaId);
        return ResponseEntity.ok(reservaDto);
    }

    // Get all reservas REST API
    @GetMapping
    public ResponseEntity<List<ReservaDto>> getAllReservas() {
        List<ReservaDto> reservas = reservaService.getAllReservas(); // Cambiado a List<ReservaDto>
        return ResponseEntity.ok(reservas);
    }

    // Update a reserva by ID REST API
    @PutMapping("/{reservaId}")
    public ResponseEntity<ReservaDto> updateReserva(@PathVariable("reservaId") Long reservaId,
            @RequestBody ReservaDto updatedReserva) {
        ReservaDto reservaDto = reservaService.updateReserva(reservaId, updatedReserva);
        return ResponseEntity.ok(reservaDto);
    }

    // Delete a reserva by ID REST API
    @DeleteMapping("/{reservaId}")
    public ResponseEntity<String> deleteReserva(@PathVariable("reservaId") Long reservaId) {
        reservaService.deleteReserva(reservaId);
        return ResponseEntity.ok("Reserva eliminada correctamente");
    }

    @GetMapping("/disponibles")
    public List<ReservaDto> getAvailableReservations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReserva) {
        return reservaService.getAvailableReservationsForDate(fechaReserva);
    }

    @PutMapping("/estado/actualizar")
    public ResponseEntity<String> actualizarEstados() {
        reservaService.actualizarReservas();
        return ResponseEntity.ok("Reservas actualizadas correctamente.");
    }

}