package com.example.backend_integrador.entity.Reservation;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import com.example.backend_integrador.dto.ReservaDto;
import com.example.backend_integrador.enums.ReservaEstado;
import com.example.backend_integrador.service.email.EmailService;
import com.example.backend_integrador.service.ReservaService;

@Component
public class ReservaScheduler {

    private final ReservaService reservaService;
    private final EmailService emailService;

    public ReservaScheduler(ReservaService reservaService, EmailService emailService) {
        this.reservaService = reservaService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 9 * * *") // Ejecuta todos los días a las 9 AM
    public void enviarRecordatorios() {
        LocalDate manana = LocalDate.now().plusDays(1);
        List<ReservaDto> reservas = reservaService.getReservasByFechaAndEstado(manana, ReservaEstado.PENDIENTE);

        for (ReservaDto reserva : reservas) {
            try {
                emailService.sendReminderEmail(reserva);
            } catch (Exception e) {
                System.err.println("Error enviando recordatorio para la reserva ID: " + reserva.getReservaId());
                e.printStackTrace();
            }
        }
    }
}
