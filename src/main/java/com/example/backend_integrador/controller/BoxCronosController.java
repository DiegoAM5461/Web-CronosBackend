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

import com.example.backend_integrador.dto.BoxCronosDto;
import com.example.backend_integrador.service.BoxCronosService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = {
    "https://frontend-cronos.vercel.app"
})
@AllArgsConstructor
@RestController
@RequestMapping("/api/box-cronos")
public class BoxCronosController {

    private static final Logger logger = LoggerFactory.getLogger(BoxCronosController.class);

    private BoxCronosService boxCronosService;

    // Create a new boxCronos REST API
    @PostMapping
    public ResponseEntity<BoxCronosDto> createBoxCronos(@RequestBody BoxCronosDto boxCronosDto) {
        logger.info("POST /api/box-cronos - Creando un nuevo BoxCronos");
        logger.debug("Datos del BoxCronos a crear: {}", boxCronosDto);
        BoxCronosDto savedBoxCronos = boxCronosService.createBoxCronos(boxCronosDto);
        logger.info("BoxCronos creado con éxito: {}", savedBoxCronos);
        return new ResponseEntity<>(savedBoxCronos, HttpStatus.CREATED);
    }

    // Get a boxCronos by ID REST API
    @GetMapping("/{boxId}")
    public ResponseEntity<BoxCronosDto> getBoxCronosById(@PathVariable("boxId") Long boxId) {
        logger.info("GET /api/box-cronos/{} - Obtener BoxCronos por ID", boxId);
        BoxCronosDto boxCronosDto = boxCronosService.getBoxCronosById(boxId);
        if (boxCronosDto != null) {
            logger.debug("BoxCronos encontrado: {}", boxCronosDto);
        } else {
            logger.warn("No se encontró un BoxCronos con ID: {}", boxId);
        }
        return ResponseEntity.ok(boxCronosDto);
    }

    // Get all boxCronos REST API
    @GetMapping
    public ResponseEntity<List<BoxCronosDto>> getAllBoxCronos() {
        logger.info("GET /api/box-cronos - Obtener todos los BoxCronos");
        List<BoxCronosDto> boxCronosList = boxCronosService.getAllBoxCronos();
        logger.debug("Cantidad de BoxCronos encontrados: {}", boxCronosList.size());
        return ResponseEntity.ok(boxCronosList);
    }

    // Update a boxCronos by ID REST API
    @PutMapping("/{boxId}")
    public ResponseEntity<BoxCronosDto> updateBoxCronos(@PathVariable("boxId") Long boxId,
                                                        @RequestBody BoxCronosDto updatedBoxCronos) {
        logger.info("PUT /api/box-cronos/{} - Actualizando BoxCronos", boxId);
        logger.debug("Datos para actualizar: {}", updatedBoxCronos);
        BoxCronosDto boxCronosDto = boxCronosService.updateBoxCronos(boxId, updatedBoxCronos);
        logger.info("BoxCronos actualizado con éxito: {}", boxCronosDto);
        return ResponseEntity.ok(boxCronosDto);
    }

    // Delete a boxCronos by ID REST API
    @DeleteMapping("/{boxId}")
    public ResponseEntity<String> deleteBoxCronos(@PathVariable("boxId") Long boxId) {
        logger.info("DELETE /api/box-cronos/{} - Eliminando BoxCronos", boxId);
        boxCronosService.deleteBoxCronos(boxId);
        logger.info("BoxCronos con ID {} eliminado correctamente", boxId);
        return ResponseEntity.ok("BoxCronos eliminado correctamente");
    }
}
