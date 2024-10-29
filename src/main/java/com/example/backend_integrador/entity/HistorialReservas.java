package com.example.backend_integrador.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "historial_reservas")
public class HistorialReservas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historialId", nullable = false)
    private Long historialId;

    @Column(name = "fechaCambio", nullable = false)
    private LocalDate fechaCambio;

    @Column(name = "estadoFinal", nullable = false)
    private String estadoFinal;

    // Relación con Reserva (FK)
    @ManyToOne
    @JoinColumn(name = "reservaId", nullable = false)
    private Reserva reserva;

    // Relación con Client para obtener el cliente que hizo el cambio
    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false)
    private Client client;
}