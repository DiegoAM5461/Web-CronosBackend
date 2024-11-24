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
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private Long productId;

    @Column(name = "nombreProduct")
    private String nombre;

    @Column(name = "descripcionProduct", nullable = true)
    private String descripcion;

    @Column(name = "precioProduct")
    private Double precio;

    @Column(name = "disponibilidadProduct")
    private Boolean disponibilidad;

    @Column(name = "stockProduct", nullable = false)
    private Integer stock;

    @Column(name = "direccionImg")
    private String direccionImg;

    // Establecer el mapeo para la fk
    @ManyToOne
    @JoinColumn(name = "idCategory", nullable = false)
    private Category category;
}
