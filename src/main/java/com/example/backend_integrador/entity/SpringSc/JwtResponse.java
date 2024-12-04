package com.example.backend_integrador.entity.SpringSc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private String username; // Nuevo campo para devolver el nombre de usuario

}
