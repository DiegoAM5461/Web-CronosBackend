package com.example.backend_integrador.mapper;

import org.springframework.stereotype.Component;

import com.example.backend_integrador.dto.HistorialOrdersDto;
import com.example.backend_integrador.entity.HistorialOrders;
import com.example.backend_integrador.entity.Orders;

@Component
public class HistorialOrdersMapper {

    private final OrdersMapper ordersMapper;

    public HistorialOrdersMapper(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    public HistorialOrdersDto toDto(HistorialOrders entity) {
        return HistorialOrdersDto.builder()
                .historialOrdersId(entity.getHistorialOrdersId())
                .orders(ordersMapper.toDto(entity.getOrders())) // Usar la instancia inyectada
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public HistorialOrders toEntity(HistorialOrdersDto dto, Orders orders) {
        return HistorialOrders.builder()
                .orders(orders)
                .fechaCreacion(dto.getFechaCreacion())
                .build();
    }
}
