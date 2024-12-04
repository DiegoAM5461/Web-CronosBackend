package com.example.backend_integrador.service.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.backend_integrador.dto.ReservaDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReservationDetails(String to, ReservaDto reservaDto) throws MessagingException {
        String subject = "Detalles de su Reserva";
        String text = "<div style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>"
                + "<div style='max-width: 600px; margin: 0 auto; border: 1px solid #ddd; border-radius: 5px; padding: 20px;'>"
                + "<h2 style='color: #007BFF; text-align: center;'>Detalles de su Reserva</h2>"
                + "<p>Estimado/a <strong>" + reservaDto.getPrimerNombre() + " " + reservaDto.getPrimerApellido()
                + "</strong>,</p>"
                + "<p>Gracias por realizar su reserva con nosotros. Aquí están los detalles:</p>"
                + "<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>"
                + "<tr style='background-color: #f8f9fa;'>"
                + "<th style='padding: 10px; border: 1px solid #ddd; text-align: left;'>Campo</th>"
                + "<th style='padding: 10px; border: 1px solid #ddd; text-align: left;'>Detalle</th>"
                + "</tr>"
                + "<tr>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>Fecha</td>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>" + reservaDto.getFechaReserva() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>Hora de Inicio</td>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>" + reservaDto.getHoraInicio() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>Hora de Fin</td>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>" + reservaDto.getHoraFin() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>Box Reservado</td>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>"
                + reservaDto.getBoxId() + " (Capacidad: " + reservaDto.getBoxCapacidad() + ")</td>"
                + "</tr>"
                + "</table>"
                + "<p style='margin-top: 20px;'>Esperamos verlo pronto en <strong>Cronos Restobar</strong>.</p>"
                + "<p style='text-align: center; color: #555;'>Atentamente,</p>"
                + "<p style='text-align: center; font-weight: bold; color: #007BFF;'>Cronos Restobar</p>"
                + "</div>"
                + "</div>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // Contenido HTML

        mailSender.send(message);
    }

    public void sendReminderEmail(ReservaDto reservaDto) throws MessagingException {
        String subject = "Recordatorio de su Reserva";
        String text = "<div style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>"
                + "<div style='max-width: 600px; margin: 0 auto; border: 1px solid #ddd; border-radius: 5px; padding: 20px;'>"
                + "<h2 style='color: #FF5733; text-align: center;'>Recordatorio de su Reserva</h2>"
                + "<p>Estimado/a <strong>" + reservaDto.getPrimerNombre() + " " + reservaDto.getPrimerApellido()
                + "</strong>,</p>"
                + "<p>Le recordamos que tiene una reserva pendiente con nosotros:</p>"
                + "<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>"
                + "<tr style='background-color: #f8f9fa;'>"
                + "<th style='padding: 10px; border: 1px solid #ddd; text-align: left;'>Campo</th>"
                + "<th style='padding: 10px; border: 1px solid #ddd; text-align: left;'>Detalle</th>"
                + "</tr>"
                + "<tr>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>Fecha</td>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>" + reservaDto.getFechaReserva() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>Hora de Inicio</td>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>" + reservaDto.getHoraInicio() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>Hora de Fin</td>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>" + reservaDto.getHoraFin() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>Box Reservado</td>"
                + "<td style='padding: 10px; border: 1px solid #ddd;'>"
                + reservaDto.getBoxId() + " (Capacidad: " + reservaDto.getBoxCapacidad() + ")</td>"
                + "</tr>"
                + "</table>"
                + "<p style='margin-top: 20px;'>¡Estamos ansiosos por recibirlo!</p>"
                + "<p style='text-align: center; color: #555;'>Atentamente,</p>"
                + "<p style='text-align: center; font-weight: bold; color: #FF5733;'>Cronos Restobar</p>"
                + "</div>"
                + "</div>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(reservaDto.getEmail());
        helper.setSubject(subject);
        helper.setText(text, true); // Contenido HTML

        mailSender.send(message);
    }
}
