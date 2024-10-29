package com.example.backend_integrador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservaId", nullable = false)
    private Long reservaId;

    @Column(name = "fechaReserva", nullable = false)
    private String fechaReserva;

    @Column(name = "horaInicio", nullable = false)
    private String horaInicio;

    @Column(name = "horaFin", nullable = false)
    private String horaFin;

    @Column(name = "estadoReserva", nullable = false)
    private String estadoReserva;

    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "boxId", nullable = false)
    private BoxCronos box;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<HistorialReservas> historialReservas;
}