package com.example.backend_integrador.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_integrador.dto.TableCronosDto;
import com.example.backend_integrador.service.TableCronosService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/api/table-cronos")
public class TableCronosController {

    private static final Logger logger = LoggerFactory.getLogger(TableCronosController.class);

    private TableCronosService tableCronosService;

    // Create a new table REST API
    @PostMapping
    public ResponseEntity<TableCronosDto> createTableCronos(@RequestBody TableCronosDto tableCronosDto) {
        logger.info("POST /api/table-cronos - Creando nueva mesa");
        logger.debug("Datos de la mesa a crear: {}", tableCronosDto);
        TableCronosDto savedTableCronos = tableCronosService.createTableCronos(tableCronosDto);
        logger.info("Mesa creada con éxito: {}", savedTableCronos);
        return new ResponseEntity<>(savedTableCronos, HttpStatus.CREATED);
    }

    // Get a table by ID REST API
    @GetMapping("/{tableCronosId}")
    public ResponseEntity<TableCronosDto> getTableCronosById(@PathVariable("tableCronosId") Long tableCronosId) {
        logger.info("GET /api/table-cronos/{} - Obtener mesa por ID", tableCronosId);
        TableCronosDto tableCronosDto = tableCronosService.getTableCronosById(tableCronosId);
        if (tableCronosDto != null) {
            logger.debug("Mesa encontrada: {}", tableCronosDto);
        } else {
            logger.warn("No se encontró una mesa con ID: {}", tableCronosId);
        }
        return ResponseEntity.ok(tableCronosDto);
    }

    // Get all table REST API
    @GetMapping
    public ResponseEntity<List<TableCronosDto>> getAllTableCronos() {
        logger.info("GET /api/table-cronos - Obtener todas las mesas");
        List<TableCronosDto> tableCronosList = tableCronosService.getAllTableCronos();
        logger.debug("Cantidad de mesas encontradas: {}", tableCronosList.size());
        return ResponseEntity.ok(tableCronosList);
    }

    // Update a table by ID REST API
    @PutMapping("/{tableCronosId}")
    public ResponseEntity<TableCronosDto> updateBoxCronos(@PathVariable("tableCronosId") Long tableCronosId,
                                                          @RequestBody TableCronosDto updatedTableCronos) {
        logger.info("PUT /api/table-cronos/{} - Actualizando mesa", tableCronosId);
        logger.debug("Datos para actualizar: {}", updatedTableCronos);
        TableCronosDto tableCronosDto = tableCronosService.upTableCronos(tableCronosId, updatedTableCronos);
        logger.info("Mesa actualizada con éxito: {}", tableCronosDto);
        return ResponseEntity.ok(tableCronosDto);
    }

    // Delete a table by ID REST API
    @DeleteMapping("/{tableCronosId}")
    public ResponseEntity<String> deleteTableCronos(@PathVariable("tableCronosId") Long tableCronosId) {
        logger.info("DELETE /api/table-cronos/{} - Eliminando mesa", tableCronosId);
        try {
            tableCronosService.deleteTableCronos(tableCronosId);
            logger.info("Mesa con ID {} eliminada correctamente", tableCronosId);
            return ResponseEntity.ok("Mesa eliminada correctamente");
        } catch (Exception e) {
            logger.error("Error al eliminar la mesa con ID {}: {}", tableCronosId, e.getMessage());
            throw e;
        }
    }
}
