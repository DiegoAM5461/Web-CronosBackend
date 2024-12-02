package com.example.backend_integrador.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialOrdersDto {

    private Long historialOrdersId;
    private OrdersDto orders;
    private LocalDateTime fechaCreacion;
}
