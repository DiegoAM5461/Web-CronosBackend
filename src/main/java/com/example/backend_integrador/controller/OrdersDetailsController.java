package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.service.OrdersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(ordersDetailsService.createOrdersDetails(dto));
    }

    @GetMapping("/{ordersDetailsId}")
    public ResponseEntity<OrdersDetailsDto> getOrdersDetailsById(@PathVariable Long ordersDetailsId) {
        return ResponseEntity.ok(ordersDetailsService.getOrdersDetailsById(ordersDetailsId));
    }

    @GetMapping("/orders/{ordersId}")
    public ResponseEntity<List<OrdersDetailsDto>> getDetailsByOrder(@PathVariable Long ordersId) {
        return ResponseEntity.ok(ordersDetailsService.getDetailsByOrder(ordersId));
    }

    @DeleteMapping("/{ordersDetailsId}")
    public ResponseEntity<Void> deleteOrdersDetails(@PathVariable Long ordersDetailsId) {
        ordersDetailsService.deleteOrdersDetails(ordersDetailsId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ordersId}/remove-product/{productId}")
    public ResponseEntity<OrdersDetailsDto> removeProductFromOrder(
            @PathVariable Long ordersId,
            @PathVariable Long productId,
            @RequestParam Integer quantityToRemove) {
        return ResponseEntity.ok(ordersDetailsService.removeProductFromOrder(ordersId, productId, quantityToRemove));
    }
}
