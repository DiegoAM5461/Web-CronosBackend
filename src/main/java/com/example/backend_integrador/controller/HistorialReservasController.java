package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.HistorialReservasDto;
import com.example.backend_integrador.service.HistorialReservasService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial-reservas")
@AllArgsConstructor
public class HistorialReservasController {

    private HistorialReservasService historialReservasService;

    @PostMapping
    public ResponseEntity<HistorialReservasDto> createHistorialReservas(@RequestBody HistorialReservasDto historialReservasDto) {
        HistorialReservasDto savedHistorial = historialReservasService.createHistorialReservas(historialReservasDto);
        return new ResponseEntity<>(savedHistorial, HttpStatus.CREATED);
    }

    @GetMapping("/{historialId}")
    public ResponseEntity<HistorialReservasDto> getHistorialReservasById(@PathVariable Long historialId) {
        HistorialReservasDto historialReservasDto = historialReservasService.getHistorialReservasById(historialId);
        return ResponseEntity.ok(historialReservasDto);
    }

    @GetMapping
    public ResponseEntity<List<HistorialReservasDto>> getAllHistorialReservas() {
        List<HistorialReservasDto> historialReservasList = historialReservasService.getAllHistorialReservas();
        return ResponseEntity.ok(historialReservasList);
    }
}
