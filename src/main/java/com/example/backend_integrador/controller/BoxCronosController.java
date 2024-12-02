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

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/api/box-cronos")
public class BoxCronosController {

    private static final Logger logger = LoggerFactory.getLogger(BoxCronosController.class);
    private BoxCronosService boxCronosService;

    // Create a new boxCronos REST API
    @PostMapping
    public ResponseEntity<BoxCronosDto> createBoxCronos(@RequestBody BoxCronosDto boxCronosDto) {
        logger.info("Creating new BoxCronos with data: {}", boxCronosDto);
        BoxCronosDto savedBoxCronos = boxCronosService.createBoxCronos(boxCronosDto);
        logger.info("BoxCronos created successfully with ID: {}", savedBoxCronos.getBoxId());
        return new ResponseEntity<>(savedBoxCronos, HttpStatus.CREATED);
    }

    // Get a boxCronos by ID REST API
    @GetMapping("/{boxId}")
    public ResponseEntity<BoxCronosDto> getBoxCronosById(@PathVariable("boxId") Long boxId) {
        logger.info("Fetching BoxCronos with ID: {}", boxId);
        BoxCronosDto boxCronosDto = boxCronosService.getBoxCronosById(boxId);
        logger.info("Fetched BoxCronos: {}", boxCronosDto);
        return ResponseEntity.ok(boxCronosDto);
    }

    // Get all boxCronos REST API
    @GetMapping
    public ResponseEntity<List<BoxCronosDto>> getAllBoxCronos() {
        logger.info("Fetching all BoxCronos...");
        List<BoxCronosDto> boxCronosList = boxCronosService.getAllBoxCronos();
        logger.info("Fetched {} BoxCronos", boxCronosList.size());
        return ResponseEntity.ok(boxCronosList);
    }

    // Update a boxCronos by ID REST API
    @PutMapping("/{boxId}")
    public ResponseEntity<BoxCronosDto> updateBoxCronos(@PathVariable("boxId") Long boxId,
                                                        @RequestBody BoxCronosDto updatedBoxCronos) {
        logger.info("Updating BoxCronos with ID: {} and data: {}", boxId, updatedBoxCronos);
        BoxCronosDto boxCronosDto = boxCronosService.updateBoxCronos(boxId, updatedBoxCronos);
        logger.info("BoxCronos updated successfully: {}", boxCronosDto);
        return ResponseEntity.ok(boxCronosDto);
    }

    // Delete a boxCronos by ID REST API
    @DeleteMapping("/{boxId}")
    public ResponseEntity<String> deleteBoxCronos(@PathVariable("boxId") Long boxId) {
        logger.info("Deleting BoxCronos with ID: {}", boxId);
        boxCronosService.deleteBoxCronos(boxId);
        logger.info("BoxCronos with ID: {} deleted successfully", boxId);
        return ResponseEntity.ok("BoxCronos eliminado correctamente");
    }
}
