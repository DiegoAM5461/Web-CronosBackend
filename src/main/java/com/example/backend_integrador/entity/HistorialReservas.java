package com.example.backend_integrador.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.backend_integrador.enums.ReservaEstado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    @Column(name = "horaCambio", nullable = false)
    private LocalTime horaCambio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoFinal", nullable = false)
    private ReservaEstado estadoFinal;

    // Relación con Reserva (FK)
    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    // Nuevo campo para clientId
    @Column(name = "client_id", nullable = true)  // Ajusta `nullable` según tus necesidades
    private Long clientId;
}
