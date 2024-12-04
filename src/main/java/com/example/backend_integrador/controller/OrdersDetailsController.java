package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.service.OrdersDetailsService;
import com.example.backend_integrador.service.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(OrdersDetailsController.class);

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDetailsService ordersDetailsService;

    @PostMapping
    public ResponseEntity<OrdersDetailsDto> createOrdersDetails(@RequestBody OrdersDetailsDto dto) {
        logger.info("POST /api/orders-details - Creando un nuevo detalle de orden");
        logger.debug("Datos del detalle de orden a crear: {}", dto);
        OrdersDetailsDto createdDetails = ordersDetailsService.createOrdersDetails(dto);
        logger.info("Detalle de orden creado con éxito: {}", createdDetails);
        return ResponseEntity.ok(createdDetails);
    }

    @GetMapping("/{ordersDetailsId}")
    public ResponseEntity<OrdersDetailsDto> getOrdersDetailsById(@PathVariable Long ordersDetailsId) {
        logger.info("GET /api/orders-details/{} - Obtener detalle de orden por ID", ordersDetailsId);
        OrdersDetailsDto details = ordersDetailsService.getOrdersDetailsById(ordersDetailsId);
        if (details != null) {
            logger.debug("Detalle de orden encontrado: {}", details);
        } else {
            logger.warn("No se encontró un detalle de orden con ID: {}", ordersDetailsId);
        }
        return ResponseEntity.ok(details);
    }

    @GetMapping("/orders/{ordersId}")
    public ResponseEntity<List<OrdersDetailsDto>> getDetailsByOrder(@PathVariable Long ordersId) {
        logger.info("GET /api/orders-details/orders/{} - Obtener detalles de orden por ID de orden", ordersId);
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByOrder(ordersId);
        logger.debug("Cantidad de detalles encontrados: {}", details.size());
        return ResponseEntity.ok(details);
    }

    @DeleteMapping("/{ordersDetailsId}")
    public ResponseEntity<Void> deleteOrdersDetails(@PathVariable Long ordersDetailsId) {
        logger.info("DELETE /api/orders-details/{} - Eliminando detalle de orden", ordersDetailsId);
        ordersDetailsService.deleteOrdersDetails(ordersDetailsId);
        logger.info("Detalle de orden con ID {} eliminado correctamente", ordersDetailsId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ordersId}/remove-product/{productId}")
    public ResponseEntity<OrdersDetailsDto> removeProductFromOrder(
            @PathVariable Long ordersId,
            @PathVariable Long productId,
            @RequestParam Integer quantityToRemove) {
        logger.info("PATCH /api/orders-details/{}/remove-product/{} - Removiendo producto de la orden", ordersId, productId);
        logger.debug("Cantidad a remover: {}", quantityToRemove);
        OrdersDetailsDto updatedDetails = ordersDetailsService.removeProductFromOrder(ordersId, productId, quantityToRemove);
        logger.info("Producto removido de la orden con éxito: {}", updatedDetails);
        return ResponseEntity.ok(updatedDetails);
    }

    @PutMapping("/{ordersDetailsId}")
    public ResponseEntity<OrdersDetailsDto> updateOrdersDetails(
            @PathVariable Long ordersDetailsId,
            @RequestBody OrdersDetailsDto updatedDetails) {
        logger.info("PUT /api/orders-details/{} - Actualizando detalle de orden", ordersDetailsId);
        logger.debug("Datos para actualizar: {}", updatedDetails);
        OrdersDetailsDto updatedOrderDetails = ordersDetailsService.updateOrdersDetails(ordersDetailsId, updatedDetails);
        logger.info("Detalle de orden actualizado con éxito: {}", updatedOrderDetails);
        return ResponseEntity.ok(updatedOrderDetails);
    }

    @GetMapping("/by-box/{boxId}/{ordersId}")
    public ResponseEntity<?> getDetailsByBoxAndOrder(@PathVariable Long boxId, @PathVariable Long ordersId) {
        logger.info("GET /api/orders-details/by-box/{}/{} - Obtener detalles de orden por box y orden", boxId, ordersId);
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByBoxAndOrder(boxId, ordersId);
        if (details.isEmpty()) {
            logger.warn("No se encontraron detalles de orden para boxId {} y ordersId {}", boxId, ordersId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No order details found for the given boxId and ordersId");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ordersId", ordersId);
        response.put("ordersEstado", ordersService.getOrderEstadoById(ordersId));
        response.put("ordersDetails", details);

        logger.debug("Detalles de orden encontrados para boxId {} y ordersId {}: {}", boxId, ordersId, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-table/{tableCronosId}/{ordersId}")
    public ResponseEntity<?> getDetailsByTableAndOrder(@PathVariable Long tableCronosId, @PathVariable Long ordersId) {
        logger.info("GET /api/orders-details/by-table/{}/{} - Obtener detalles de orden por mesa y orden", tableCronosId, ordersId);
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByTableAndOrder(tableCronosId, ordersId);
        if (details.isEmpty()) {
            logger.warn("No se encontraron detalles de orden para tableCronosId {} y ordersId {}", tableCronosId, ordersId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No order details found for the given tableCronosId and ordersId");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ordersId", ordersId);
        response.put("ordersEstado", ordersService.getOrderEstadoById(ordersId));
        response.put("ordersDetails", details);

        logger.debug("Detalles de orden encontrados para tableCronosId {} y ordersId {}: {}", tableCronosId, ordersId, response);
        return ResponseEntity.ok(response);
    }
}
