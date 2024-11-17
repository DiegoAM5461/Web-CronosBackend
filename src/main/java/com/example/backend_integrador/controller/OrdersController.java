package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping
    public ResponseEntity<OrdersDto> createOrder(@RequestBody OrdersDto ordersDto) {
        return ResponseEntity.ok(ordersService.createOrder(ordersDto));
    }

    @GetMapping("/{ordersId}")
    public ResponseEntity<OrdersDto> getOrderById(@PathVariable("ordersId") Long ordersId) {
        return ResponseEntity.ok(ordersService.getOrderById(ordersId));
    }

    @GetMapping
    public ResponseEntity<List<OrdersDto>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @DeleteMapping("/{ordersId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("ordersId") Long ordersId) {
        ordersService.deleteOrder(ordersId);
        return ResponseEntity.noContent().build();
    }
}
