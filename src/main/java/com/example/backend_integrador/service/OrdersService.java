package com.example.backend_integrador.service;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.enums.OrdersEstado;

import java.util.List;

public interface OrdersService {
    OrdersDto createOrder(OrdersDto ordersDto);

    OrdersDto getOrderById(Long id);

    List<OrdersDto> getAllOrders();

    void deleteOrder(Long id);

    void cancelarPedido(Long ordersId);

    void confirmarPedido(Long ordersId);

    void autoCancelOrCompleteExpiredOrders();

    List<OrdersDto> getOrdersByBoxId(Long boxId);

    List<OrdersDto> getOrdersByTableCronosId(Long tableCronosId);

    void completarPedido(Long ordersId);

    List<OrdersDto> getOrdersByStatuses(List<OrdersEstado> estados);

    OrdersEstado getOrderEstadoById(Long ordersId);


}
