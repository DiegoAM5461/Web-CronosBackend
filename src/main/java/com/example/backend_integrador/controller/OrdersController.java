package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.enums.OrdersEstado;
import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.service.OrdersService;
import com.example.backend_integrador.service.OrdersDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDetailsService ordersDetailsService;

    @PostMapping
    public ResponseEntity<OrdersDto> createOrder(@RequestBody OrdersDto ordersDto) {
        logger.info("Creating new order with data: {}", ordersDto);
        OrdersDto createdOrder = ordersService.createOrder(ordersDto);
        logger.info("Order created successfully with ID: {}", createdOrder.getOrdersId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{ordersId}")
    public ResponseEntity<OrdersDto> getOrderById(@PathVariable Long ordersId) {
        logger.info("Fetching order with ID: {}", ordersId);
        OrdersDto order = ordersService.getOrderById(ordersId);
        logger.info("Fetched order: {}", order);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrdersDto>> getAllOrders() {
        logger.info("Fetching all orders...");
        List<OrdersDto> orders = ordersService.getAllOrders();
        logger.info("Fetched {} orders", orders.size());
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{ordersId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long ordersId) {
        logger.info("Deleting order with ID: {}", ordersId);
        ordersService.deleteOrder(ordersId);
        logger.info("Order with ID: {} deleted successfully", ordersId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ordersId}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable Long ordersId) {
        logger.info("Cancelling order with ID: {}", ordersId);
        ordersService.cancelarPedido(ordersId);
        logger.info("Order with ID: {} cancelled successfully", ordersId);
        return ResponseEntity.ok("Pedido cancelado y stock liberado.");
    }

    @GetMapping("/{ordersId}/carrito")
    public ResponseEntity<Map<String, Object>> getCartDetails(@PathVariable Long ordersId) {
        logger.info("Fetching cart details for order ID: {}", ordersId);
        OrdersDto order = ordersService.getOrderById(ordersId);
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByOrder(ordersId);

        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("details", details);

        logger.info("Fetched cart details for order ID: {}", ordersId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{ordersId}/confirmar")
    public ResponseEntity<String> confirmarPedido(@PathVariable Long ordersId) {
        logger.info("Confirming order with ID: {}", ordersId);
        ordersService.confirmarPedido(ordersId);
        logger.info("Order with ID: {} confirmed successfully", ordersId);
        return ResponseEntity.ok("Pedido confirmado exitosamente.");
    }

    @PatchMapping("/{ordersId}/completar")
    public ResponseEntity<String> completarPedido(@PathVariable Long ordersId) {
        logger.info("Completing order with ID: {}", ordersId);
        ordersService.completarPedido(ordersId);
        logger.info("Order with ID: {} completed successfully", ordersId);
        return ResponseEntity.ok("Pedido completado exitosamente.");
    }

    @GetMapping("/orders-by-box/{boxId}")
    public ResponseEntity<?> getOrdersByBox(@PathVariable Long boxId) {
        logger.info("Fetching orders for box ID: {}", boxId);
        List<OrdersDto> orders = ordersService.getOrdersByBoxId(boxId);

        List<Map<String, Object>> response = new ArrayList<>();
        for (OrdersDto order : orders) {
            logger.info("Processing order with ID: {}", order.getOrdersId());
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("ordersId", order.getOrdersId());
            orderInfo.put("ordersEstado", order.getOrdersEstado());

            List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByOrder(order.getOrdersId());
            orderInfo.put("ordersDetails", details);

            response.add(orderInfo);
        }

        logger.info("Fetched orders for box ID: {}", boxId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders-by-table/{tableId}")
    public ResponseEntity<?> getOrdersByTable(@PathVariable Long tableId) {
        logger.info("Fetching orders for table ID: {}", tableId);
        List<OrdersDto> orders = ordersService.getOrdersByTableCronosId(tableId);

        List<Map<String, Object>> response = new ArrayList<>();
        for (OrdersDto order : orders) {
            logger.info("Processing order with ID: {}", order.getOrdersId());
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("ordersId", order.getOrdersId());
            orderInfo.put("ordersEstado", order.getOrdersEstado());

            List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByOrder(order.getOrdersId());
            orderInfo.put("ordersDetails", details);

            response.add(orderInfo);
        }

        logger.info("Fetched orders for table ID: {}", tableId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<OrdersDto>> getOrdersByStatuses(@RequestParam String estados) {
        logger.info("Fetching orders by statuses: {}", estados);
        List<OrdersEstado> estadosList = Arrays.stream(estados.split(","))
                .map(OrdersEstado::valueOf)
                .collect(Collectors.toList());
        List<OrdersDto> orders = ordersService.getOrdersByStatuses(estadosList);

        logger.info("Fetched {} orders for statuses: {}", orders.size(), estados);
        return ResponseEntity.ok(orders);
    }
}
