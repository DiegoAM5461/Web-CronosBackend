package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.service.OrdersService;
import com.example.backend_integrador.service.OrdersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDetailsService ordersDetailsService; // Fix: Ahora está correctamente autowireado

    @PostMapping
    public ResponseEntity<OrdersDto> createOrder(@RequestBody OrdersDto ordersDto) {
        return ResponseEntity.ok(ordersService.createOrder(ordersDto));
    }

    @GetMapping("/{ordersId}")
    public ResponseEntity<OrdersDto> getOrderById(@PathVariable Long ordersId) {
        return ResponseEntity.ok(ordersService.getOrderById(ordersId));
    }

    @GetMapping
    public ResponseEntity<List<OrdersDto>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @DeleteMapping("/{ordersId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long ordersId) {
        ordersService.deleteOrder(ordersId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ordersId}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable Long ordersId) {
        ordersService.cancelarPedido(ordersId);
        return ResponseEntity.ok("Pedido cancelado y stock liberado.");
    }

    @GetMapping("/{ordersId}/carrito")
    public ResponseEntity<Map<String, Object>> getCartDetails(@PathVariable Long ordersId) {
        OrdersDto order = ordersService.getOrderById(ordersId);
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByOrder(ordersId);

        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("details", details);

        return ResponseEntity.ok(response);
    }
}
