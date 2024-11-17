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

import com.example.backend_integrador.dto.TableCronosDto;
import com.example.backend_integrador.service.TableCronosService;

import lombok.AllArgsConstructor;


@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/api/table-cronos")
public class TableCronosController {

    private TableCronosService tableCronosService;

    // Create a new table REST API
    @PostMapping
    public ResponseEntity<TableCronosDto> createTableCronos(@RequestBody TableCronosDto tableCronosDto) {
        TableCronosDto savedBoxCronos = tableCronosService.createTableCronos(tableCronosDto);
        return new ResponseEntity<>(savedBoxCronos, HttpStatus.CREATED);
    }

    // Get a table by ID REST API
    @GetMapping("/{tableCronosId}")
    public ResponseEntity<TableCronosDto> getTableCronosById(@PathVariable("tableCronosId") Long tableCronosId) {
        TableCronosDto tableCronosDto = tableCronosService.getTableCronosById(tableCronosId);
        return ResponseEntity.ok(tableCronosDto);
    }

    // Get all table REST API
    @GetMapping
    public ResponseEntity<List<TableCronosDto>> getAllTableCronos(){
        List<TableCronosDto> tableCronosList = tableCronosService.getAllTableCronos();
        return ResponseEntity.ok(tableCronosList);
    }

    // Update a table by ID REST API
    @PutMapping("/{tableCronosId}")
    public ResponseEntity<TableCronosDto> updateBoxCronos(@PathVariable("tableCronosId") Long tableCronosId,
                                                        @RequestBody TableCronosDto updatedTableCronos){
        TableCronosDto tableCronosDto = tableCronosService.upTableCronos(tableCronosId, updatedTableCronos);
        return ResponseEntity.ok(tableCronosDto);
    }

    // Delete a table by ID REST API
    @DeleteMapping("/{tableCronosId}")
    public ResponseEntity<String> deleteTableCronos(@PathVariable("tableCronosId") Long tableCronosId){
        tableCronosService.deleteTableCronos(tableCronosId);
        return ResponseEntity.ok("Mesa eliminada correctamente");
    }
}
