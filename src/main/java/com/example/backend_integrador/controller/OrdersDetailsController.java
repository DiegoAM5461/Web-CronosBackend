package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.service.OrdersDetailsService;
import com.example.backend_integrador.service.OrdersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders-details")
public class OrdersDetailsController {

    @Autowired
    private OrdersService ordersService;

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

    @PutMapping("/{ordersDetailsId}")
    public ResponseEntity<OrdersDetailsDto> updateOrdersDetails(
            @PathVariable Long ordersDetailsId,
            @RequestBody OrdersDetailsDto updatedDetails) {
        return ResponseEntity.ok(ordersDetailsService.updateOrdersDetails(ordersDetailsId, updatedDetails));
    }

    @GetMapping("/by-box/{boxId}/{ordersId}")
    public ResponseEntity<?> getDetailsByBoxAndOrder(@PathVariable Long boxId, @PathVariable Long ordersId) {
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByBoxAndOrder(boxId, ordersId);
        if (details.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No order details found for the given boxId and ordersId");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ordersId", ordersId);
        response.put("ordersEstado", ordersService.getOrderEstadoById(ordersId)); // Método en OrdersService
        response.put("ordersDetails", details);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-table/{tableCronosId}/{ordersId}")
    public ResponseEntity<?> getDetailsByTableAndOrder(@PathVariable Long tableCronosId, @PathVariable Long ordersId) {
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByTableAndOrder(tableCronosId, ordersId);
        if (details.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No order details found for the given tableCronosId and ordersId");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ordersId", ordersId);
        response.put("ordersEstado", ordersService.getOrderEstadoById(ordersId)); // Método en OrdersService
        response.put("ordersDetails", details);

        return ResponseEntity.ok(response);
    }

}
