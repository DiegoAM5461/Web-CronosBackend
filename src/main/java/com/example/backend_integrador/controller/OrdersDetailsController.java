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
        logger.info("Creating order detail: {}", dto);
        OrdersDetailsDto createdDetail = ordersDetailsService.createOrdersDetails(dto);
        logger.info("Order detail created successfully with ID: {}", createdDetail.getOrdersDetailsId());
        return ResponseEntity.ok(createdDetail);
    }

    @GetMapping("/{ordersDetailsId}")
    public ResponseEntity<OrdersDetailsDto> getOrdersDetailsById(@PathVariable Long ordersDetailsId) {
        logger.info("Fetching order detail with ID: {}", ordersDetailsId);
        OrdersDetailsDto detail = ordersDetailsService.getOrdersDetailsById(ordersDetailsId);
        logger.info("Fetched order detail: {}", detail);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/orders/{ordersId}")
    public ResponseEntity<List<OrdersDetailsDto>> getDetailsByOrder(@PathVariable Long ordersId) {
        logger.info("Fetching order details for order ID: {}", ordersId);
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByOrder(ordersId);
        logger.info("Fetched {} order details for order ID: {}", details.size(), ordersId);
        return ResponseEntity.ok(details);
    }

    @DeleteMapping("/{ordersDetailsId}")
    public ResponseEntity<Void> deleteOrdersDetails(@PathVariable Long ordersDetailsId) {
        logger.info("Deleting order detail with ID: {}", ordersDetailsId);
        ordersDetailsService.deleteOrdersDetails(ordersDetailsId);
        logger.info("Order detail with ID: {} deleted successfully", ordersDetailsId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ordersId}/remove-product/{productId}")
    public ResponseEntity<OrdersDetailsDto> removeProductFromOrder(
            @PathVariable Long ordersId,
            @PathVariable Long productId,
            @RequestParam Integer quantityToRemove) {
        logger.info("Removing product with ID: {} from order ID: {} with quantity: {}", productId, ordersId, quantityToRemove);
        OrdersDetailsDto updatedDetail = ordersDetailsService.removeProductFromOrder(ordersId, productId, quantityToRemove);
        logger.info("Product removed successfully from order ID: {}", ordersId);
        return ResponseEntity.ok(updatedDetail);
    }

    @PutMapping("/{ordersDetailsId}")
    public ResponseEntity<OrdersDetailsDto> updateOrdersDetails(
            @PathVariable Long ordersDetailsId,
            @RequestBody OrdersDetailsDto updatedDetails) {
        logger.info("Updating order detail with ID: {} with data: {}", ordersDetailsId, updatedDetails);
        OrdersDetailsDto detail = ordersDetailsService.updateOrdersDetails(ordersDetailsId, updatedDetails);
        logger.info("Order detail with ID: {} updated successfully", ordersDetailsId);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/by-box/{boxId}/{ordersId}")
    public ResponseEntity<?> getDetailsByBoxAndOrder(@PathVariable Long boxId, @PathVariable Long ordersId) {
        logger.info("Fetching order details for box ID: {} and order ID: {}", boxId, ordersId);
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByBoxAndOrder(boxId, ordersId);
        if (details.isEmpty()) {
            logger.info("No order details found for box ID: {} and order ID: {}", boxId, ordersId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No order details found for the given boxId and ordersId");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ordersId", ordersId);
        response.put("ordersEstado", ordersService.getOrderEstadoById(ordersId));
        response.put("ordersDetails", details);

        logger.info("Fetched {} order details for box ID: {} and order ID: {}", details.size(), boxId, ordersId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-table/{tableCronosId}/{ordersId}")
    public ResponseEntity<?> getDetailsByTableAndOrder(@PathVariable Long tableCronosId, @PathVariable Long ordersId) {
        logger.info("Fetching order details for table ID: {} and order ID: {}", tableCronosId, ordersId);
        List<OrdersDetailsDto> details = ordersDetailsService.getDetailsByTableAndOrder(tableCronosId, ordersId);
        if (details.isEmpty()) {
            logger.info("No order details found for table ID: {} and order ID: {}", tableCronosId, ordersId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No order details found for the given tableCronosId and ordersId");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ordersId", ordersId);
        response.put("ordersEstado", ordersService.getOrderEstadoById(ordersId));
        response.put("ordersDetails", details);

        logger.info("Fetched {} order details for table ID: {} and order ID: {}", details.size(), tableCronosId, ordersId);
        return ResponseEntity.ok(response);
    }
}
