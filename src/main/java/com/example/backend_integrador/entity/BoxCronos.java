package com.example.backend_integrador.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "box_cronos")
public class BoxCronos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boxId")
    private Long boxId;

    @Column(name = "boxNumero")
    private String boxNumero;

    @Column(name = "boxCapacidad")
    private Integer boxCapacidad;

    @Column(name = "boxEstado")
    private String boxEstado;  //valores: "1", "2", "3"
}