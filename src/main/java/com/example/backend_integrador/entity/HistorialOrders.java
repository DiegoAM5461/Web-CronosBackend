package com.example.backend_integrador.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "historial_orders")
public class HistorialOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historialOrdersId;

    @ManyToOne
    @JoinColumn(name = "orders_id", nullable = false)
    private Orders orders;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}
