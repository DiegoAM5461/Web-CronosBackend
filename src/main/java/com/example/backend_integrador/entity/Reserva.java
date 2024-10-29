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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservaId")
    private Long reservaId;

    @Column(name = "fechaReserva")
    private String fechaReserva;

    @Column(name = "horaInicio")
    private String horaInicio;

    @Column(name = "horaFin")
    private String horaFin;

    @Column(name = "estadoReserva")
    private String estadoReserva;

    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "boxId", nullable = false)
    private BoxCronos box;

    @Column(name = "disponibilidad")
    private String disponibilidad; // Nueva columna para almacenar el estado de la disponibilidad del box
}
