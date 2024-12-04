package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.enums.OrdersEstado;
import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.service.OrdersService;
import com.example.backend_integrador.service.OrdersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDetailsService ordersDetailsService;

    @PostMapping
    public ResponseEntity<OrdersDto> createOrder(@RequestBody OrdersDto ordersDto) {
        try {
            OrdersDto createdOrder = ordersService.createOrder(ordersDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
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

    @PatchMapping("/{ordersId}/confirmar")
    public ResponseEntity<String> confirmarPedido(@PathVariable Long ordersId) {
        ordersService.confirmarPedido(ordersId); // Este método será eliminado
        return ResponseEntity.ok("Pedido confirmado exitosamente.");
    }

    @PatchMapping("/{ordersId}/completar")
    public ResponseEntity<String> completarPedido(@PathVariable Long ordersId) {
        ordersService.completarPedido(ordersId);
        return ResponseEntity.ok("Pedido completado exitosamente.");
    }

    @GetMapping("/orders-by-box/{boxId}")
    public ResponseEntity<?> getOrdersByBox(@PathVariable Long boxId) {
        List<OrdersDto> orders = ordersService.getOrdersByBoxId(boxId);
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found for the given boxId");
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (OrdersDto order : orders) {
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("ordersId", order.getOrdersId());
            orderInfo.put("ordersEstado", order.getOrdersEstado());

            List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByOrder(order.getOrdersId());
            orderInfo.put("ordersDetails", details);

            response.add(orderInfo);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders-by-table/{tableId}")
    public ResponseEntity<?> getOrdersByTable(@PathVariable Long tableId) {
        List<OrdersDto> orders = ordersService.getOrdersByTableCronosId(tableId);
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found for the given tableId");
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (OrdersDto order : orders) {
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("ordersId", order.getOrdersId());
            orderInfo.put("ordersEstado", order.getOrdersEstado());

            List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByOrder(order.getOrdersId());
            orderInfo.put("ordersDetails", details);

            response.add(orderInfo);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<OrdersDto>> getOrdersByStatuses(@RequestParam String estados) {
        List<OrdersEstado> estadosList = Arrays.stream(estados.split(","))
                .map(OrdersEstado::valueOf)
                .collect(Collectors.toList());
        List<OrdersDto> orders = ordersService.getOrdersByStatuses(estadosList);

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(orders);
    }

}
