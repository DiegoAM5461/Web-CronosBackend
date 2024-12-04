package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.HistorialOrdersDto;
import com.example.backend_integrador.service.HistorialOrdersService;
import com.example.backend_integrador.service.export.ExcelExportService;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(HistorialOrdersController.class);

    private final ExcelExportService excelExportService;
    private final HistorialOrdersService service;

    @GetMapping
    public ResponseEntity<List<HistorialOrdersDto>> getHistorialOrdersByDateRange(
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end) {
        logger.info("GET /api/historial-orders - Obtener historial de órdenes entre {} y {}", start, end);
        List<HistorialOrdersDto> historialOrders = service.getHistorialOrdersByFechaCreacionBetween(start, end);
        logger.debug("Órdenes encontradas: {}", historialOrders.size());
        return ResponseEntity.ok(historialOrders);
    }

    @GetMapping("/analysis/daily-with-days")
    public ResponseEntity<List<Map<String, Object>>> getDailyAnalysisWithDays(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        logger.info("GET /api/historial-orders/analysis/daily-with-days - Análisis diario entre {} y {}", start, end);
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        List<Map<String, Object>> analysis = service.getDailyAnalysisWithDayOfWeek(startDate, endDate);
        logger.debug("Datos del análisis diario: {}", analysis);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/export/detailed-excel")
    public ResponseEntity<byte[]> exportDetailedOrdersToExcel(
            @RequestParam("start") String start,
            @RequestParam("end") String end) throws IOException {
        logger.info("GET /api/historial-orders/export/detailed-excel - Exportar detalles de órdenes entre {} y {}", start, end);
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        List<Map<String, Object>> detailedOrders = service.getDetailedOrders(startDate, endDate);
        logger.debug("Datos detallados de las órdenes: {}", detailedOrders);

        byte[] excelFile = excelExportService.generateDetailedOrdersReport(detailedOrders);
        logger.info("Reporte detallado generado con éxito");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=detailed-orders.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportDailyOrdersToExcel(
            @RequestParam("start") String start,
            @RequestParam("end") String end) throws IOException {
        logger.info("GET /api/historial-orders/export/excel - Exportar análisis diario entre {} y {}", start, end);
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        List<Map<String, Object>> analysis = service.getDailyAnalysisWithDayOfWeek(startDate, endDate);
        logger.debug("Datos del análisis diario para exportación: {}", analysis);

        byte[] excelFile = excelExportService.generateDailyOrdersReport(analysis);
        logger.info("Reporte diario generado con éxito");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily-orders-report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }
}
