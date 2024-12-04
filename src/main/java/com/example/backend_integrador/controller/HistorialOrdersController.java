package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.HistorialOrdersDto;
import com.example.backend_integrador.service.HistorialOrdersService;
import com.example.backend_integrador.service.export.ExcelExportService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/api/historial-orders")
public class HistorialOrdersController {

    private final ExcelExportService excelExportService;
    private final HistorialOrdersService service;

    @GetMapping
    public ResponseEntity<List<HistorialOrdersDto>> getHistorialOrdersByDateRange(
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end) {
        return ResponseEntity.ok(service.getHistorialOrdersByFechaCreacionBetween(start, end));
    }

    @GetMapping("/analysis/daily-with-days")
    public ResponseEntity<List<Map<String, Object>>> getDailyAnalysisWithDays(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        List<Map<String, Object>> analysis = service.getDailyAnalysisWithDayOfWeek(startDate, endDate);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/export/detailed-excel")
    public ResponseEntity<byte[]> exportDetailedOrdersToExcel(
            @RequestParam("start") String start,
            @RequestParam("end") String end) throws IOException {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        List<Map<String, Object>> detailedOrders = service.getDetailedOrders(startDate, endDate);

        // Agregar log para inspeccionar los datos
        System.out.println("Datos detallados: " + detailedOrders);

        byte[] excelFile = excelExportService.generateDetailedOrdersReport(detailedOrders);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=detailed-orders.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportDailyOrdersToExcel(
            @RequestParam("start") String start,
            @RequestParam("end") String end) throws IOException {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        List<Map<String, Object>> analysis = service.getDailyAnalysisWithDayOfWeek(startDate, endDate);

        byte[] excelFile = excelExportService.generateDailyOrdersReport(analysis);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily-orders-report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }
}

