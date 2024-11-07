package com.example.backend_integrador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> 
                authorizeRequests.anyRequest().permitAll() // Permitir todos los accesos temporalmente
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configurar CORS con una fuente específica
            .csrf(csrf -> csrf.disable()); // Deshabilitar CSRF si no se maneja autenticación basada en sesión

        return http.build();
    }

    // Configuración específica para CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Orígenes permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Métodos HTTP permitidos
        configuration.setAllowCredentials(true); // Permitir credenciales (cookies, auth headers)
        configuration.setAllowedHeaders(List.of("*")); // Permitir todos los encabezados

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
