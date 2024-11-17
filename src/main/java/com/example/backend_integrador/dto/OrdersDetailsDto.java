package com.example.backend_integrador.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDetailsDto {
    private Long ordersDetailsId;
    private Long ordersId;
    private Long productId;
    private Integer quantity;
    private Double subtotal;
}
