package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.OrdersDetails;
import com.example.backend_integrador.entity.Product;

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
                .boxId(ordersDetails.getOrders().getBox() != null ? ordersDetails.getOrders().getBox().getBoxId() : null)
                .tableCronosId(ordersDetails.getOrders().getTableCronos() != null ? ordersDetails.getOrders().getTableCronos().getTableCronosId() : null)
                .build();
    }
    

    public static OrdersDetails toEntity(OrdersDetailsDto dto, Orders orders, Product product) {
        return OrdersDetails.builder()
                .ordersDetailsId(dto.getOrdersDetailsId())
                .orders(orders)
                .product(product)
                .quantity(dto.getQuantity())
                .subtotal(dto.getSubtotal())
                .build();
    }
}

