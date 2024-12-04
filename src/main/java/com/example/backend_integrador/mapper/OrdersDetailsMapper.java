package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.OrdersDetails;
import com.example.backend_integrador.entity.Product;
import com.example.backend_integrador.enums.OrdersDetailsEstado;

import org.springframework.stereotype.Component;

@Component
public class OrdersDetailsMapper {

    public static OrdersDetailsDto toDto(OrdersDetails ordersDetails) {
        if (ordersDetails == null) {
            throw new IllegalArgumentException("OrdersDetails no puede ser nulo");
        }
        Product product = ordersDetails.getProduct();
        return OrdersDetailsDto.builder()
                .ordersDetailsId(ordersDetails.getOrdersDetailsId())
                .ordersId(ordersDetails.getOrders().getOrdersId())
                .productId(product != null ? product.getProductId() : null)
                .productName(product != null ? product.getNombre() : null)
                .productImage(product != null ? product.getDireccionImg() : null)
                .price(product != null ? product.getPrecio() : null)
                .quantity(ordersDetails.getQuantity())
                .subtotal(ordersDetails.getSubtotal())
                .boxId(ordersDetails.getOrders().getBox() != null ? ordersDetails.getOrders().getBox().getBoxId()
                        : null)
                .tableCronosId(ordersDetails.getOrders().getTableCronos() != null
                        ? ordersDetails.getOrders().getTableCronos().getTableCronosId()
                        : null)
                .estado(ordersDetails.getEstado()) // Nuevo campo
                .build();
    }

    public static OrdersDetails toEntity(OrdersDetailsDto dto, Orders orders, Product product) {
        if (dto == null) {
            throw new IllegalArgumentException("OrdersDetailsDto no puede ser nulo");
        }
        return OrdersDetails.builder()
                .ordersDetailsId(dto.getOrdersDetailsId())
                .orders(orders)
                .product(product)
                .quantity(dto.getQuantity())
                .subtotal(dto.getSubtotal())
                .estado(dto.getEstado() != null ? dto.getEstado() : OrdersDetailsEstado.PENDIENTE) // Asignar estado o
                                                                                                   // predeterminado
                .build();
    }

}
