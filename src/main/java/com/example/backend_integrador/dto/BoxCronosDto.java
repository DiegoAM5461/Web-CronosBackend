package com.example.backend_integrador.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoxCronosDto {
    private Long boxId;
    private String boxNumero;
    private Integer boxCapacidad;
}