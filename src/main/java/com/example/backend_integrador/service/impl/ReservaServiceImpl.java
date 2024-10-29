package com.example.backend_integrador.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend_integrador.dto.ReservaDto;
import com.example.backend_integrador.entity.Reserva;
import com.example.backend_integrador.entity.Client;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.repository.ReservaRepository;
import com.example.backend_integrador.repository.ClientRepository;
import com.example.backend_integrador.repository.BoxCronosRepository;
import com.example.backend_integrador.service.ReservaService;
import com.example.backend_integrador.mapper.ReservaMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private ReservaRepository reservaRepository;
    private ClientRepository clientRepository;
    private BoxCronosRepository boxCronosRepository;

    @Override
    public ReservaDto createReserva(ReservaDto reservaDto) {
        // Buscar o crear un cliente usando el ID
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

        // Buscar el box usando el ID
        BoxCronos box = boxCronosRepository.findById(reservaDto.getBoxId())
                .orElseThrow(() -> new ResourceNotFoundException("El box con id: " + reservaDto.getBoxId() + " no existe"));

        // Verificar el estado del box antes de crear la reserva
        if (box.getBoxEstado().equals("1") || box.getBoxEstado().equals("2")) {
            throw new IllegalStateException("El box no está disponible para reservar. Estado actual: " + box.getBoxEstado());
        }

        // Mapear la reserva
        Reserva reserva = ReservaMapper.mapToReserva(reservaDto, client, box);
        Reserva savedReserva = reservaRepository.save(reserva);

        // Retornar la reserva guardada como DTO
        return ReservaMapper.mapToReservaDto(savedReserva);
    }

    @Override
    public ReservaDto getReservaById(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("La reserva con id: " + reservaId + " no existe"));
        return ReservaMapper.mapToReservaDto(reserva);
    }

    @Override
    public List<ReservaDto> getAllReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream().map(ReservaMapper::mapToReservaDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReservaDto updateReserva(Long reservaId, ReservaDto updatedReserva) {
        // Buscar la reserva existente
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow(
            () -> new ResourceNotFoundException("La reserva con id: " + reservaId + " no existe")
        );

        // Buscar el cliente usando el ID del cliente proporcionado
        Client client = clientRepository.findById(updatedReserva.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con id: " + updatedReserva.getClientId() + " no existe"));

        // Buscar el box usando el ID del box proporcionado
        BoxCronos box = boxCronosRepository.findById(updatedReserva.getBoxId())
                .orElseThrow(() -> new ResourceNotFoundException("El box con id: " + updatedReserva.getBoxId() + " no existe"));

        // Actualizar los atributos de la reserva
        reserva.setFechaReserva(updatedReserva.getFechaReserva());
        reserva.setHoraInicio(updatedReserva.getHoraInicio());
        reserva.setHoraFin(updatedReserva.getHoraFin());
        reserva.setEstadoReserva(updatedReserva.getEstadoReserva());
        reserva.setClient(client);
        reserva.setBox(box);

        Reserva updatedReservaObj = reservaRepository.save(reserva);
        return ReservaMapper.mapToReservaDto(updatedReservaObj);
    }

    @Override
    public void deleteReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow(
                () -> new ResourceNotFoundException("La reserva con el id: " + reservaId + " no existe"));
        reservaRepository.delete(reserva);
    }
}