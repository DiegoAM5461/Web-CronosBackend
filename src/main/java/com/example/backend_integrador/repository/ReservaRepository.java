package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}