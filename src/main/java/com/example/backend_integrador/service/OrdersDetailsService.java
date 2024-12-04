package com.example.backend_integrador.service;

import com.example.backend_integrador.dto.OrdersDetailsDto;

import java.util.List;

public interface OrdersDetailsService {

    OrdersDetailsDto createOrdersDetails(OrdersDetailsDto dto);

    OrdersDetailsDto getOrdersDetailsById(Long id);

    List<OrdersDetailsDto> getDetailsByOrder(Long ordersId); // Método agregado

    OrdersDetailsDto updateOrdersDetails(Long id, OrdersDetailsDto updatedDto);

    void deleteOrdersDetails(Long id);

    OrdersDetailsDto removeProductFromOrder(Long ordersId, Long productId, Integer quantityToRemove);

    List<OrdersDetailsDto> getDetailsByBoxAndOrder(Long boxId, Long ordersId);

    List<OrdersDetailsDto> getDetailsByTableAndOrder(Long tableCronosId, Long ordersId);

}
