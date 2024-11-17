package com.example.backend_integrador.dto;

import com.example.backend_integrador.enums.OrdersEstado;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersDto {
    private Long ordersId;
    private Long boxId;
    private Long tableCronosId;
    private OrdersEstado ordersEstado;
    private Double ordersTotal;
}
