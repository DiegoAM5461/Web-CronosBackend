package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.HistorialOrdersDto;
import com.example.backend_integrador.mapper.HistorialOrdersMapper;
import com.example.backend_integrador.repository.HistorialOrdersRepository;
import com.example.backend_integrador.service.HistorialOrdersService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HistorialOrdersServiceImpl implements HistorialOrdersService {

    private final HistorialOrdersRepository repository;
    private final HistorialOrdersMapper mapper;

    public HistorialOrdersServiceImpl(HistorialOrdersRepository repository, HistorialOrdersMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<HistorialOrdersDto> getHistorialOrdersByFechaCreacionBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findByFechaCreacionBetween(start, end).stream()
                .map(mapper::toDto) // Ahora usas la instancia inyectada
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getDailyAnalysisWithDayOfWeek(LocalDateTime start, LocalDateTime end) {
        return repository.findDailyAnalysis(start, end).stream()
                .map(record -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("fecha", record.get("fecha"));
                    result.put("total_ordenes", record.get("total_ordenes"));
                    // Convertir la fecha al día de la semana
                    LocalDate date = LocalDate.parse(record.get("fecha").toString());
                    String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
                    result.put("dia_semana", dayOfWeek);
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getDetailedOrders(LocalDateTime start, LocalDateTime end) {
        return repository.findDetailedOrders(start, end).stream()
                .map(record -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("fecha", record.get("fecha"));
                    // Convertir ordersId explícitamente a Long
                    result.put("ordersId",
                            record.get("ordersId") != null ? Long.parseLong(record.get("ordersId").toString()) : null);
                    result.put("estado", record.get("estado"));
                    result.put("boxId", record.get("boxId") != null ? record.get("boxId").toString() : "N/A");
                    result.put("tableCronosId",
                            record.get("tableCronosId") != null ? record.get("tableCronosId").toString() : "N/A");
                    result.put("productName",
                            record.get("productName") != null ? record.get("productName").toString() : "N/A");
                    result.put("quantity",
                            record.get("quantity") != null ? Integer.parseInt(record.get("quantity").toString()) : 0);
                    result.put("price",
                            record.get("price") != null ? Double.parseDouble(record.get("price").toString()) : 0.0);
                    return result;
                })
                .collect(Collectors.toList());
    }

}