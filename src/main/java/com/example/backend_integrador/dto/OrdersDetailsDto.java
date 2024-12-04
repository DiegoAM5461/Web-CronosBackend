package com.example.backend_integrador.dto;

import com.example.backend_integrador.enums.OrdersDetailsEstado; // Importa el enum
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDetailsDto {

    private Long ordersDetailsId;

    @NotNull(message = "El ID del pedido no puede ser nulo")
    private Long ordersId;

    @NotNull(message = "El ID del producto no puede ser nulo")
    private Long productId;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    @NotNull(message = "El subtotal no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal no puede ser negativo")
    private Double subtotal;

    // Nuevos campos para vinculación con box o table
    private Long boxId;
    private Long tableCronosId;

    private String productName;
    private String productImage;
    private Double price;

    // Nuevo campo para el estado del detalle
    private OrdersDetailsEstado estado; // Estado del producto en el pedido
}
