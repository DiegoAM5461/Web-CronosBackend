package com.example.backend_integrador.controller;

import java.util.List;

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


@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/box-cronos")
public class BoxCronosController {

    private BoxCronosService boxCronosService;

    // Create a new boxCronos REST API
    @PostMapping
    public ResponseEntity<BoxCronosDto> createBoxCronos(@RequestBody BoxCronosDto boxCronosDto) {
        BoxCronosDto savedBoxCronos = boxCronosService.createBoxCronos(boxCronosDto);
        return new ResponseEntity<>(savedBoxCronos, HttpStatus.CREATED);
    }

    // Get a boxCronos by ID REST API
    @GetMapping("{boxId}")
    public ResponseEntity<BoxCronosDto> getBoxCronosById(@PathVariable("boxId") Long boxId) {
        BoxCronosDto boxCronosDto = boxCronosService.getBoxCronosById(boxId);
        return ResponseEntity.ok(boxCronosDto);
    }

    // Get all boxCronos REST API
    @GetMapping
    public ResponseEntity<List<BoxCronosDto>> getAllBoxCronos(){
        List<BoxCronosDto> boxCronosList = boxCronosService.getAllBoxCronos();
        return ResponseEntity.ok(boxCronosList);
    }

    // Update a boxCronos by ID REST API
    @PutMapping("{boxId}")
    public ResponseEntity<BoxCronosDto> updateBoxCronos(@PathVariable("boxId") Long boxId,
                                                        @RequestBody BoxCronosDto updatedBoxCronos){
        BoxCronosDto boxCronosDto = boxCronosService.updateBoxCronos(boxId, updatedBoxCronos);
        return ResponseEntity.ok(boxCronosDto);
    }

    // Delete a boxCronos by ID REST API
    @DeleteMapping("{boxId}")
    public ResponseEntity<String> deleteBoxCronos(@PathVariable("boxId") Long boxId){
        boxCronosService.deleteBoxCronos(boxId);
        return ResponseEntity.ok("BoxCronos eliminado correctamente");
    }
}