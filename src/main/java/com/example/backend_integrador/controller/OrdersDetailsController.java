package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.service.OrdersDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders-details")
public class OrdersDetailsController {

    @Autowired
    private OrdersDetailsService ordersDetailsService;

    @PostMapping
    public ResponseEntity<OrdersDetailsDto> createOrdersDetails(@RequestBody OrdersDetailsDto dto) {
        OrdersDetailsDto savedDto = ordersDetailsService.createOrdersDetails(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @GetMapping("/{ordersDetailsId}")
    public ResponseEntity<OrdersDetailsDto> getOrdersDetailsById(@PathVariable("ordersDetailsId") Long ordersDetailsId) {
        return ResponseEntity.ok(ordersDetailsService.getOrdersDetailsById(ordersDetailsId));
    }

    @GetMapping
    public ResponseEntity<List<OrdersDetailsDto>> getAllOrdersDetails() {
        return ResponseEntity.ok(ordersDetailsService.getAllOrdersDetails());
    }

    @PutMapping("/{ordersDetailsId}")
    public ResponseEntity<OrdersDetailsDto> updateOrdersDetails(
            @PathVariable("ordersDetailsId") Long ordersDetailsId, @RequestBody OrdersDetailsDto updatedDto) {
        return ResponseEntity.ok(ordersDetailsService.updateOrdersDetails(ordersDetailsId, updatedDto));
    }

    @DeleteMapping("/{ordersDetailsId}")
    public ResponseEntity<Void> deleteOrdersDetails(@PathVariable("ordersDetailsId") Long ordersDetailsId) {
        ordersDetailsService.deleteOrdersDetails(ordersDetailsId);
        return ResponseEntity.noContent().build();
    }
}
