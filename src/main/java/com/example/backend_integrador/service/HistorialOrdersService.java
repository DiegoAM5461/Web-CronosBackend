package com.example.backend_integrador.service;

import com.example.backend_integrador.dto.HistorialOrdersDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface HistorialOrdersService {
    List<HistorialOrdersDto> getHistorialOrdersByFechaCreacionBetween(LocalDateTime start, LocalDateTime end);

    List<Map<String, Object>> getDailyAnalysisWithDayOfWeek(LocalDateTime start, LocalDateTime end);

    List<Map<String, Object>> getDetailedOrders(LocalDateTime start, LocalDateTime end);

}
