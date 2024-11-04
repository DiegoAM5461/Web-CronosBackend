package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.Reserva;
import com.example.backend_integrador.enums.ReservaEstado;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByBoxBoxIdAndFechaReserva(Long boxId, LocalDate fechaReserva);

    List<Reserva> findAllByEstadoReserva(ReservaEstado estadoReserva);

    List<Reserva> findByFechaReserva(LocalDate fechaReserva);

    List<Reserva> findByFechaReservaAndEstadoReservaNot(LocalDate fechaReserva, ReservaEstado estadoReserva);

}   