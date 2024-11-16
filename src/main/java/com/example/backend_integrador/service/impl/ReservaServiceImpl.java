package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.ReservaDto;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.entity.Client;
import com.example.backend_integrador.entity.Reserva;
import com.example.backend_integrador.entity.HistorialReservas;
import com.example.backend_integrador.enums.ReservaEstado;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.mapper.ReservaMapper;
import com.example.backend_integrador.repository.BoxCronosRepository;
import com.example.backend_integrador.repository.ClientRepository;
import com.example.backend_integrador.repository.ReservaRepository;
import com.example.backend_integrador.repository.HistorialReservasRepository;
import com.example.backend_integrador.service.ReservaService;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservaServiceImpl implements ReservaService {

        private final ReservaRepository reservaRepository;
        private final ClientRepository clientRepository;
        private final BoxCronosRepository boxCronosRepository;
        private final HistorialReservasRepository historialReservasRepository;

        private static final LocalTime HORARIO_INICIO = LocalTime.of(22, 0); // 10 PM
        private static final LocalTime HORARIO_FIN = LocalTime.of(3, 0); // 3 AM del día siguiente

        @Override
        @Transactional
        public ReservaDto createReserva(ReservaDto reservaDto) {
                // Asignar la hora de inicio y fin predeterminada
                reservaDto.setHoraInicio(HORARIO_INICIO);
                reservaDto.setHoraFin(HORARIO_FIN);

                // Verificar si el box está disponible en la fecha solicitada
                List<Reserva> reservasExistentes = reservaRepository.findByBoxBoxIdAndFechaReserva(
                                reservaDto.getBoxId(),
                                reservaDto.getFechaReserva());

                // Comprobar si alguna reserva existente en esa fecha no está cancelada
                boolean reservaActivaExistente = reservasExistentes.stream()
                                .anyMatch(r -> r.getEstadoReserva() != ReservaEstado.CANCELADA);

                if (reservaActivaExistente) {
                        throw new IllegalStateException("El box no está disponible en la fecha solicitada.");
                }

                // Buscar el cliente por el clientId
                Client client = clientRepository.findById(reservaDto.getClientId()).orElse(null);

                // Si el cliente no existe, crearlo y verificar campos obligatorios
                if (clientRepository.existsById(reservaDto.getClientId())) {
                        client = clientRepository.findById(reservaDto.getClientId()).orElse(null);
                } else {
                        // Crear y guardar el nuevo cliente solo si no existe
                        Client nuevoCliente = new Client();
                        nuevoCliente.setClientId(reservaDto.getClientId()); // Si quieres asignar manualmente
                        nuevoCliente.setPrimerNombre(reservaDto.getPrimerNombre());
                        nuevoCliente.setPrimerApellido(reservaDto.getPrimerApellido());
                        nuevoCliente.setEmail(reservaDto.getEmail());
                        nuevoCliente.setTelefono(reservaDto.getTelefono());

                        client = clientRepository.save(nuevoCliente);
                }

                // Obtener el box correspondiente
                BoxCronos box = boxCronosRepository.findById(reservaDto.getBoxId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "El box con el id_box: " + reservaDto.getBoxId() + " no existe"));

                // Crear la reserva y asignar cliente y box
                Reserva reserva = ReservaMapper.mapToReserva(reservaDto, client, box);
                reserva.setEstadoReserva(ReservaEstado.PENDIENTE);

                // Guardar la reserva en la base de datos
                Reserva savedReserva = reservaRepository.save(reserva);

                // Registrar el cambio inicial en el historial
                registrarCambioHistorial(savedReserva, ReservaEstado.PENDIENTE);

                // Retornar la reserva en formato DTO
                return ReservaMapper.mapToReservaDto(savedReserva);
        }

        @Override
        public ReservaDto getReservaById(Long reservaId) {
                Reserva reserva = reservaRepository.findById(reservaId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Reserva no encontrada con id: " + reservaId));

                // Usar el mapper para convertir la entidad en DTO
                return ReservaMapper.mapToReservaDto(reserva);
        }

        @Override
        public List<ReservaDto> getAllReservas() {
                List<Reserva> reservas = reservaRepository.findAll();
                return reservas.stream().map(ReservaMapper::mapToReservaDto).toList();
        }

        @Override
        @Transactional
        public ReservaDto updateReserva(Long reservaId, ReservaDto updatedReserva) {
                Reserva reserva = reservaRepository.findById(reservaId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "La reserva con el id: " + reservaId + " no existe"));

                reserva.setFechaReserva(updatedReserva.getFechaReserva());
                reserva.setHoraInicio(HORARIO_INICIO);
                reserva.setHoraFin(HORARIO_FIN);
                reserva.setEstadoReserva(updatedReserva.getEstadoReserva());

                Reserva updatedEntity = reservaRepository.save(reserva);

                // Registrar el cambio en el historial
                registrarCambioHistorial(updatedEntity, updatedReserva.getEstadoReserva());

                return ReservaMapper.mapToReservaDto(updatedEntity);
        }

        @Override
        @Transactional
        public void deleteReserva(Long reservaId) {
                Reserva reserva = reservaRepository.findById(reservaId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "La reserva con el id: " + reservaId + " no existe"));

                // Cambia el estado a CANCELADA en lugar de eliminar
                reserva.setEstadoReserva(ReservaEstado.CANCELADA);
                reservaRepository.save(reserva);

                // Registrar el cambio en el historial con estado CANCELADA
                registrarCambioHistorial(reserva, ReservaEstado.CANCELADA);
        }

        // Método para registrar cambios en el historial
        private void registrarCambioHistorial(Reserva reserva, ReservaEstado nuevoEstado) {
                HistorialReservas historial = new HistorialReservas();
                historial.setEstadoFinal(nuevoEstado);
                historial.setFechaCambio(LocalDate.now());
                historial.setHoraCambio(LocalTime.now());
                historial.setReserva(reserva);

                // Agrega el clientId desde la entidad reserva
                historial.setClientId(reserva.getClient().getClientId());

                historialReservasRepository.save(historial);
        }

        // Tarea programada para actualizar el estado de las reservas a TERMINADA a las
        // 3:00 AM
        @Scheduled(cron = "0 0 3 * * ?")
        public void actualizarReservasTerminadas() {
                List<Reserva> reservas = reservaRepository.findAllByEstadoReserva(ReservaEstado.PENDIENTE);
                reservas.forEach(reserva -> {
                        if (reserva.getHoraFin().isBefore(LocalTime.now())) {
                                reserva.setEstadoReserva(ReservaEstado.TERMINADA);
                                reservaRepository.save(reserva);
                                registrarCambioHistorial(reserva, ReservaEstado.TERMINADA);
                        }
                });
        }

        // Implementación del método getAvailableReservationsForDate de la interfaz
        // ReservaService
        @Override
        public List<ReservaDto> getAvailableReservationsForDate(LocalDate fechaReserva) {
                List<BoxCronos> allBoxes = boxCronosRepository.findAll();

                // Obtener reservas activas para la fecha dada (excluyendo las CANCELADA)
                List<Reserva> reservasActivas = reservaRepository.findByFechaReservaAndEstadoReservaNot(fechaReserva,
                                ReservaEstado.CANCELADA);

                // Filtrar boxes disponibles excluyendo los que ya están reservados en la fecha
                // dada
                List<BoxCronos> availableBoxes = allBoxes.stream()
                                .filter(box -> reservasActivas.stream()
                                                .noneMatch(reserva -> reserva.getBox().getBoxId()
                                                                .equals(box.getBoxId())))
                                .toList();

                // Crear ReservaDto solo con detalles necesarios
                return availableBoxes.stream()
                                .map(box -> new ReservaDto(null, fechaReserva, null, null, null, null,
                                                box.getBoxId(), null, null, null, null, box.getBoxCapacidad()))
                                .toList();
        }
}
