package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.ReservaDto;
import com.example.backend_integrador.entity.Reserva;
import com.example.backend_integrador.entity.Client;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.entity.HistorialReservas;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.repository.ReservaRepository;
import com.example.backend_integrador.repository.HistorialReservasRepository;
import com.example.backend_integrador.repository.ClientRepository;
import com.example.backend_integrador.repository.BoxCronosRepository;
import com.example.backend_integrador.service.ReservaService;
import com.example.backend_integrador.mapper.ReservaMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final HistorialReservasRepository historialReservasRepository;
    private final ClientRepository clientRepository;
    private final BoxCronosRepository boxCronosRepository;

    @Override
    public ReservaDto createReserva(ReservaDto reservaDto) {
        // Verificar si el cliente ya existe o crear uno nuevo
        Client client = clientRepository.findById(reservaDto.getClientId()).orElseGet(() -> {
            // Crear un nuevo cliente si no existe
            Client newClient = new Client();
            newClient.setClientId(reservaDto.getClientId());
            newClient.setPrimerNombre(reservaDto.getPrimerNombre());
            newClient.setPrimerApellido(reservaDto.getPrimerApellido());
            newClient.setEmail(reservaDto.getEmail());
            newClient.setTelefono(reservaDto.getTelefono());
            // Guardar el nuevo cliente
            return clientRepository.save(newClient);
        });

        // Buscar el box para la reserva
        BoxCronos box = boxCronosRepository.findById(reservaDto.getBoxId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Box con id: " + reservaDto.getBoxId() + " no encontrado"));

        // Verificar disponibilidad del box
        if (box.getBoxEstado().equals("1") || box.getBoxEstado().equals("2")) {
            throw new IllegalStateException(
                    "El box no está disponible para reservar. Estado actual: " + box.getBoxEstado());
        }

        // Cambiar el estado del box a "1" (reservado)
        box.setBoxEstado("1");
        boxCronosRepository.save(box);

        // Crear una nueva reserva a partir del DTO
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(reservaDto.getFechaReserva());
        reserva.setHoraInicio(reservaDto.getHoraInicio());
        reserva.setHoraFin(reservaDto.getHoraFin());
        reserva.setEstadoReserva("pendiente");
        reserva.setClient(client);
        reserva.setBox(box);

        Reserva savedReserva = reservaRepository.save(reserva);

        // Crear historial para la nueva reserva
        HistorialReservas historial = new HistorialReservas();
        historial.setReserva(savedReserva);
        historial.setClient(client);
        historial.setFechaCambio(LocalDate.now());
        historial.setEstadoFinal(obtenerEstadoFinalDeReserva(savedReserva.getEstadoReserva()));

        historialReservasRepository.save(historial);

        // Retornar la reserva guardada como DTO
        return ReservaMapper.mapToReservaDto(savedReserva);
    }

    @Override
public ReservaDto updateReserva(Long reservaId, ReservaDto reservaDto) {
    // Buscar la reserva
    Reserva reserva = reservaRepository.findById(reservaId)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva con id: " + reservaId + " no encontrada"));

    // Verificar si el estado cambió para crear un historial
    boolean estadoCambio = !reserva.getEstadoReserva().equals(reservaDto.getEstadoReserva());
    
    // Actualizar los campos de la reserva
    reserva.setEstadoReserva(reservaDto.getEstadoReserva());
    reserva.setHoraInicio(reservaDto.getHoraInicio());
    reserva.setHoraFin(reservaDto.getHoraFin());
    reserva.setFechaReserva(reservaDto.getFechaReserva());

    // Guardar la reserva actualizada
    Reserva updatedReserva = reservaRepository.save(reserva);

    // Si el estado cambió, actualizar el estado del box
    if (estadoCambio) {
        BoxCronos box = updatedReserva.getBox();
        if (reservaDto.getEstadoReserva().equals("confirmado")) {
            box.setBoxEstado("2"); // Estado "2" = Ocupado
        } else if (reservaDto.getEstadoReserva().equals("cancelado")) {
            box.setBoxEstado("3"); // Estado "3" = Disponible
        }
        boxCronosRepository.save(box);

        // Crear un nuevo historial
        HistorialReservas historial = new HistorialReservas();
        historial.setReserva(updatedReserva);
        historial.setClient(updatedReserva.getClient()); // Asociar el cliente que hizo la reserva
        historial.setFechaCambio(LocalDate.now()); // Fecha actual para el cambio
        historial.setEstadoFinal(obtenerEstadoFinalDeReserva(reservaDto.getEstadoReserva()));

        historialReservasRepository.save(historial);
    }

    // Retornar el DTO de la reserva actualizada
    return ReservaMapper.mapToReservaDto(updatedReserva);
}


    @Override
    public ReservaDto getReservaById(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con id: " + reservaId + " no encontrada"));
        return ReservaMapper.mapToReservaDto(reserva);
    }

    @Override
    public List<ReservaDto> getAllReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream().map(ReservaMapper::mapToReservaDto).collect(Collectors.toList());
    }

    @Override
    public void deleteReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con id: " + reservaId + " no encontrada"));
        reservaRepository.delete(reserva);
    }

    private String obtenerEstadoFinalDeReserva(String estadoReserva) {
        switch (estadoReserva) {
            case "pendiente":
                return "1";
            case "confirmado":
                return "2";
            case "cancelado":
                return "3";
            default:
                throw new IllegalStateException("Estado de reserva no reconocido: " + estadoReserva);
        }
    }
}
