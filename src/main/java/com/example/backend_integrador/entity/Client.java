package com.example.backend_integrador.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "client")
public class Client {
    //No se esta generando automaticamente el DNI o ID del cliente
    @Id
    @Column(name = "clientId", nullable = false)
    private Long clientId;

    @Column(name = "primerNombre", nullable = true)
    private String primerNombre;

    @Column(name = "primerApellido", nullable = true)
    private String primerApellido;

    @Column(name = "segundoApellido", nullable = true)
    private String segundoApellido;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "telefono", nullable = true)
    private String telefono;
}