package com.example.backend_integrador.entity;

import com.example.backend_integrador.enums.OrdersEstado;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long ordersId;

    @ManyToOne
    @JoinColumn(name = "box_id", referencedColumnName = "boxId", nullable = true)
    private BoxCronos box;

    @ManyToOne
    @JoinColumn(name = "table_cronos_id", referencedColumnName = "tableCronosId", nullable = true)
    private TableCronos tableCronos;

    @Enumerated(EnumType.STRING)
    @Column(name = "orders_estado", nullable = false)
    private OrdersEstado ordersEstado;

    @Column(name = "orders_total", nullable = false)
    private Double ordersTotal;
}
