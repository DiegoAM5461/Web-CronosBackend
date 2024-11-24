package com.example.backend_integrador.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableCronosDto {
    private Long tableCronosId;

    @NotBlank(message = "El número de mesa no puede estar vacío")
    private String tableNumero;

    @NotBlank(message = "El QR de la mesa no puede estar vacío")
    private String tableQR;
}
