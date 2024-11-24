package com.example.backend_integrador.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "detalle_pedido")
public class OrdersDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Long ordersDetailsId;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Product product;

    @Column(name = "cantidad_detalle", nullable = false)
    private Integer quantity;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;
}
