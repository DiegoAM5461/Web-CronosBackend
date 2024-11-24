package com.example.backend_integrador.service;

import com.example.backend_integrador.dto.OrdersDto;

import java.util.List;

public interface OrdersService {
    OrdersDto createOrder(OrdersDto ordersDto);

    OrdersDto getOrderById(Long id);

    List<OrdersDto> getAllOrders();

    void deleteOrder(Long id);

    void cancelarPedido(Long ordersId);
}
