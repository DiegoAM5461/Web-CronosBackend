package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.HistorialReservas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialReservasRepository extends JpaRepository<HistorialReservas, Long> {
}