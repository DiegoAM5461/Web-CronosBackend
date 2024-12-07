package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.enums.OrdersEstado;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.service.OrdersService;
import com.example.backend_integrador.service.OrdersDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin(origins = {
    "http://localhost:3000", 
    "https://frontend-cronos.vercel.app"
})
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDetailsService ordersDetailsService;

    // Crear una nueva orden
    @PostMapping
    public ResponseEntity<OrdersDto> createOrder(@RequestBody OrdersDto ordersDto) {
        logger.info("POST /api/orders - Creando una nueva orden");
        logger.debug("Datos de la orden a crear: {}", ordersDto);
        try {
            OrdersDto createdOrder = ordersService.createOrder(ordersDto);
            logger.info("Orden creada con éxito: {}", createdOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (Exception e) {
            logger.error("Error al crear la orden: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Obtener una orden por ID
    @GetMapping("/{ordersId}")
    public ResponseEntity<OrdersDto> getOrderById(@PathVariable Long ordersId) {
        logger.info("GET /api/orders/{} - Obtener orden por ID", ordersId);
        try {
            OrdersDto order = ordersService.getOrderById(ordersId);
            logger.info("Orden encontrada con ID {}: {}", ordersId, order);
            return ResponseEntity.ok(order);
        } catch (ResourceNotFoundException e) {
            logger.error("No se encontró una orden con ID: {}", ordersId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Obtener todas las órdenes
    @GetMapping
    public ResponseEntity<List<OrdersDto>> getAllOrders() {
        logger.info("GET /api/orders - Obtener todas las órdenes");
        List<OrdersDto> orders = ordersService.getAllOrders();
        logger.debug("Cantidad de órdenes encontradas: {}", orders.size());
        return ResponseEntity.ok(orders);
    }

    // Eliminar una orden
    @DeleteMapping("/{ordersId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long ordersId) {
        logger.info("DELETE /api/orders/{} - Eliminando orden", ordersId);
        try {
            ordersService.deleteOrder(ordersId);
            logger.info("Orden con ID {} eliminada correctamente", ordersId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            logger.error("Error al eliminar la orden con ID {}: {}", ordersId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Cancelar una orden
    @PatchMapping("/{ordersId}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable Long ordersId) {
        logger.info("PATCH /api/orders/{}/cancelar - Cancelando pedido", ordersId);
        try {
            ordersService.cancelarPedido(ordersId);
            logger.info("Pedido con ID {} cancelado exitosamente", ordersId);
            return ResponseEntity.ok("Pedido cancelado y stock liberado.");
        } catch (Exception e) {
            logger.error("Error al cancelar el pedido con ID {}: {}", ordersId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al cancelar el pedido.");
        }
    }

    // Obtener detalles del carrito de una orden
    @GetMapping("/{ordersId}/carrito")
    public ResponseEntity<Map<String, Object>> getCartDetails(@PathVariable Long ordersId) {
        logger.info("GET /api/orders/{}/carrito - Obtener detalles del carrito", ordersId);
        try {
            OrdersDto order = ordersService.getOrderById(ordersId);
            List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByOrder(ordersId);

            Map<String, Object> response = new HashMap<>();
            response.put("order", order);
            response.put("details", details);

            logger.debug("Detalles del carrito para la orden {}: {}", ordersId, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener los detalles del carrito para el pedido {}: {}", ordersId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Confirmar un pedido
    @PatchMapping("/{ordersId}/confirmar")
    public ResponseEntity<String> confirmarPedido(@PathVariable Long ordersId) {
        logger.info("PATCH /api/orders/{}/confirmar - Confirmando pedido", ordersId);
        try {
            ordersService.confirmarPedido(ordersId);
            logger.info("Pedido con ID {} confirmado exitosamente", ordersId);
            return ResponseEntity.ok("Pedido confirmado exitosamente.");
        } catch (Exception e) {
            logger.error("Error al confirmar el pedido con ID {}: {}", ordersId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al confirmar el pedido.");
        }
    }

    // Completar un pedido
    @PatchMapping("/{ordersId}/completar")
    public ResponseEntity<String> completarPedido(@PathVariable Long ordersId) {
        logger.info("PATCH /api/orders/{}/completar - Completando pedido", ordersId);
        try {
            ordersService.completarPedido(ordersId);
            logger.info("Pedido con ID {} completado exitosamente", ordersId);
            return ResponseEntity.ok("Pedido completado exitosamente.");
        } catch (Exception e) {
            logger.error("Error al completar el pedido con ID {}: {}", ordersId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al completar el pedido.");
        }
    }

    // Obtener órdenes por Box ID
    @GetMapping("/orders-by-box/{boxId}")
    public ResponseEntity<?> getOrdersByBox(@PathVariable Long boxId) {
        logger.info("GET /api/orders/orders-by-box/{} - Obtener órdenes por box", boxId);
        try {
            List<OrdersDto> orders = ordersService.getOrdersByBoxId(boxId);
            if (orders.isEmpty()) {
                logger.warn("No se encontraron órdenes para el box con ID: {}", boxId);
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

            logger.debug("Órdenes encontradas para el box {}: {}", boxId, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener órdenes para el box con ID {}: {}", boxId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Obtener órdenes por Table ID
    @GetMapping("/orders-by-table/{tableId}")
    public ResponseEntity<?> getOrdersByTable(@PathVariable Long tableId) {
        logger.info("GET /api/orders/orders-by-table/{} - Obtener órdenes por mesa", tableId);
        try {
            List<OrdersDto> orders = ordersService.getOrdersByTableCronosId(tableId);
            if (orders.isEmpty()) {
                logger.warn("No se encontraron órdenes para la mesa con ID: {}", tableId);
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

            logger.debug("Órdenes encontradas para la mesa {}: {}", tableId, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener órdenes para la mesa con ID {}: {}", tableId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Obtener órdenes por estados
    @GetMapping("/by-status")
    public ResponseEntity<List<OrdersDto>> getOrdersByStatuses(@RequestParam String estados) {
        logger.info("GET /api/orders/by-status - Obtener órdenes por estado: {}", estados);
        try {
            List<OrdersEstado> estadosList = Arrays.stream(estados.split(","))
                    .map(OrdersEstado::valueOf)
                    .collect(Collectors.toList());
            List<OrdersDto> orders = ordersService.getOrdersByStatuses(estadosList);

            if (orders.isEmpty()) {
                logger.warn("No se encontraron órdenes para los estados: {}", estadosList);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.debug("Órdenes encontradas para los estados {}: {}", estadosList, orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error al obtener órdenes por estado: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
