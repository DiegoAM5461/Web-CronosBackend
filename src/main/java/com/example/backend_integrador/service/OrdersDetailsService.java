package com.example.backend_integrador.service;

import com.example.backend_integrador.dto.OrdersDetailsDto;

import java.util.List;

public interface OrdersDetailsService {
    OrdersDetailsDto createOrdersDetails(OrdersDetailsDto dto);
    OrdersDetailsDto getOrdersDetailsById(Long id);
    List<OrdersDetailsDto> getAllOrdersDetails();
    OrdersDetailsDto updateOrdersDetails(Long id, OrdersDetailsDto updatedDto);
    void deleteOrdersDetails(Long id);
}
