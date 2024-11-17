package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.entity.OrdersDetails;
import org.springframework.stereotype.Component;

@Component
public class OrdersDetailsMapper {

    public static OrdersDetailsDto toDto(OrdersDetails ordersDetails) {
        return OrdersDetailsDto.builder()
                .ordersDetailsId(ordersDetails.getOrdersDetailsId())
                .ordersId(ordersDetails.getOrders().getOrdersId())
                .productId(ordersDetails.getProduct().getProductId())
                .quantity(ordersDetails.getQuantity())
                .subtotal(ordersDetails.getSubtotal())
                .build();
    }

    public static OrdersDetails toEntity(OrdersDetailsDto dto) {
        return OrdersDetails.builder()
                .ordersDetailsId(dto.getOrdersDetailsId())
                .quantity(dto.getQuantity())
                .subtotal(dto.getSubtotal())
                .build();
    }
}
