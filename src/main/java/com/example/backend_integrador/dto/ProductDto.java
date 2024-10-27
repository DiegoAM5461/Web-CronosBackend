package com.example.backend_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id_product;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Boolean disponibilidad;
    private Integer stock;
}
