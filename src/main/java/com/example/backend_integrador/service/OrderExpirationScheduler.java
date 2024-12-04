package com.example.backend_integrador.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderExpirationScheduler {

    private final OrdersService ordersService;

    // Ejecuta el job cada hora
    @Scheduled(cron = "0 0 * * * *") // Cada hora
    public void handleExpiredOrders() {
        ordersService.autoCancelOrCompleteExpiredOrders();
    }
}
