package com.example.backend_integrador.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long ordersId;

    @ManyToOne
    @JoinColumn(name = "box_id", referencedColumnName = "boxId", nullable = true)
    private BoxCronos box;

    @ManyToOne
    @JoinColumn(name = "table_cronos_id", referencedColumnName = "tableCronosId", nullable = true)
    private TableCronos tableCronos;

    @Enumerated(EnumType.STRING)
    private OrdersEstado ordersEstado;

    private Double ordersTotal;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder.Default
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrdersDetails> ordersDetails = new ArrayList<>();

}