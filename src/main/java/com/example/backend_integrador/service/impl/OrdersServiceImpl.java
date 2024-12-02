package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.entity.HistorialOrders;
import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.OrdersDetails;
import com.example.backend_integrador.entity.Product;
import com.example.backend_integrador.entity.TableCronos;
import com.example.backend_integrador.enums.OrdersDetailsEstado;
import com.example.backend_integrador.enums.OrdersEstado;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.mapper.OrdersMapper;
import com.example.backend_integrador.repository.BoxCronosRepository;
import com.example.backend_integrador.repository.HistorialOrdersRepository;
import com.example.backend_integrador.repository.OrdersDetailsRepository;
import com.example.backend_integrador.repository.OrdersRepository;
import com.example.backend_integrador.repository.ProductRepository;
import com.example.backend_integrador.repository.TableCronosRepository;
import com.example.backend_integrador.service.OrdersService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private static final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);
    private final HistorialOrdersRepository historialOrdersRepository;
    private final OrdersRepository ordersRepository;
    private final OrdersMapper ordersMapper;
    private final OrdersDetailsRepository ordersDetailsRepository;
    private final ProductRepository productRepository;
    private final BoxCronosRepository boxCronosRepository;
    private final TableCronosRepository tableCronosRepository;

    @Override
    public OrdersDto createOrder(OrdersDto ordersDto) {
        logger.info("Creating order: {}", ordersDto);

        if (ordersDto.getBoxId() != null && ordersDto.getTableCronosId() != null) {
            logger.info("Order validation failed: Associated with both Box and Table.");
            throw new IllegalArgumentException("Un pedido no puede estar asociado a ambos: Box y Mesa.");
        }

        if (ordersDto.getBoxId() == null && ordersDto.getTableCronosId() == null) {
            logger.info("Order validation failed: Not associated with any Box or Table.");
            throw new IllegalArgumentException("Debe asociar un Box o una Mesa al pedido.");
        }

        BoxCronos box = null;
        TableCronos tableCronos = null;

        if (ordersDto.getBoxId() != null) {
            logger.info("Validating Box ID: {}", ordersDto.getBoxId());
            box = boxCronosRepository.findById(ordersDto.getBoxId())
                    .orElseThrow(() -> new ResourceNotFoundException("El Box no existe."));
            if (ordersRepository.existsByBoxAndOrdersEstadoIn(box, List.of(OrdersEstado.PENDIENTE, OrdersEstado.CONFIRMADO))) {
                logger.info("Validation failed: Box already has an active order.");
                throw new IllegalStateException("El Box ya tiene un pedido activo.");
            }
        }

        if (ordersDto.getTableCronosId() != null) {
            logger.info("Validating Table ID: {}", ordersDto.getTableCronosId());
            tableCronos = tableCronosRepository.findById(ordersDto.getTableCronosId())
                    .orElseThrow(() -> new ResourceNotFoundException("La Mesa no existe."));
            if (ordersRepository.existsByTableCronosAndOrdersEstadoIn(tableCronos, List.of(OrdersEstado.PENDIENTE, OrdersEstado.CONFIRMADO))) {
                logger.info("Validation failed: Table already has an active order.");
                throw new IllegalStateException("La Mesa ya tiene un pedido activo.");
            }
        }

        Orders orders = ordersMapper.toEntity(ordersDto, box, tableCronos);
        orders.setOrdersEstado(OrdersEstado.PENDIENTE);
        orders.setOrdersTotal(0.0);

        Orders savedOrder = ordersRepository.save(orders);
        logger.info("Order created successfully with ID: {}", savedOrder.getOrdersId());
        return ordersMapper.toDto(savedOrder);
    }

    @Override
    public OrdersDto getOrderById(Long id) {
        logger.info("Fetching order with ID: {}", id);
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
        logger.info("Fetched order: {}", orders);
        return ordersMapper.toDto(orders);
    }

    @Override
    public List<OrdersDto> getAllOrders() {
        logger.info("Fetching all orders...");
        List<OrdersDto> ordersList = ordersRepository.findAll()
                .stream()
                .map(ordersMapper::toDto)
                .collect(Collectors.toList());
        logger.info("Fetched {} orders", ordersList.size());
        return ordersList;
    }

    @Override
    public void deleteOrder(Long id) {
        logger.info("Deleting order with ID: {}", id);
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
        ordersRepository.delete(orders);
        logger.info("Order with ID: {} deleted successfully", id);
    }

    @Override
    @Transactional
    public void cancelarPedido(Long ordersId) {
        logger.info("Cancelling order with ID: {}", ordersId);
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + ordersId));

        if (!orders.getOrdersEstado().equals(OrdersEstado.PENDIENTE)) {
            logger.info("Cannot cancel order. Current state: {}", orders.getOrdersEstado());
            throw new IllegalStateException("Solo se pueden cancelar pedidos en estado PENDIENTE.");
        }

        ordersDetailsRepository.findByOrders(orders).forEach(detail -> {
            Product product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
            productRepository.save(product);
        });

        orders.setOrdersEstado(OrdersEstado.CANCELADO);
        ordersRepository.save(orders);
        logger.info("Order with ID: {} cancelled successfully", ordersId);
    }

    @Override
    @Transactional
    public void autoCancelOrCompleteExpiredOrders() {
        logger.info("Automatically cancelling or completing expired orders...");
        LocalDateTime expiryDate = LocalDateTime.now().minusHours(24);
        List<Orders> expiredOrders = ordersRepository.findExpiredOrders(OrdersEstado.PENDIENTE, expiryDate);

        for (Orders orders : expiredOrders) {
            logger.info("Processing expired order with ID: {}", orders.getOrdersId());
            ordersDetailsRepository.findByOrders(orders).forEach(detail -> {
                Product product = detail.getProduct();
                product.setStock(product.getStock() + detail.getQuantity());
                productRepository.save(product);
            });

            orders.setOrdersEstado(OrdersEstado.CANCELADO);
            ordersRepository.save(orders);
            logger.info("Order with ID: {} set to CANCELADO", orders.getOrdersId());
        }
    }

    @Override
    public List<OrdersDto> getOrdersByBoxId(Long boxId) {
        logger.info("Fetching orders for Box ID: {}", boxId);
        List<OrdersDto> orders = ordersRepository.findByBox_BoxId(boxId)
                .stream()
                .map(ordersMapper::toDto)
                .collect(Collectors.toList());
        logger.info("Fetched {} orders for Box ID: {}", orders.size(), boxId);
        return orders;
    }

    @Override
    public List<OrdersDto> getOrdersByTableCronosId(Long tableCronosId) {
        logger.info("Fetching orders for Table ID: {}", tableCronosId);
        List<OrdersDto> orders = ordersRepository.findByTableCronos_TableCronosId(tableCronosId)
                .stream()
                .map(ordersMapper::toDto)
                .collect(Collectors.toList());
        logger.info("Fetched {} orders for Table ID: {}", orders.size(), tableCronosId);
        return orders;
    }

    @Override
    @Transactional
    public void confirmarPedido(Long ordersId) {
        logger.info("Confirming order with ID: {}", ordersId);
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        if (orders.getOrdersEstado() != OrdersEstado.PENDIENTE) {
            logger.info("Cannot confirm order. Current state: {}", orders.getOrdersEstado());
            throw new IllegalStateException("Solo se pueden confirmar pedidos en estado PENDIENTE.");
        }

        List<OrdersDetails> details = ordersDetailsRepository.findByOrders(orders);
        boolean allConfirmed = details.stream()
                .allMatch(detail -> detail.getEstado() == OrdersDetailsEstado.CONFIRMADO);

        if (!allConfirmed) {
            logger.info("Cannot confirm order. Not all products are CONFIRMED.");
            throw new IllegalStateException(
                    "No se puede confirmar un pedido con productos en estado diferente a CONFIRMADO.");
        }

        orders.setOrdersEstado(OrdersEstado.CONFIRMADO);
        ordersRepository.save(orders);
        logger.info("Order with ID: {} confirmed successfully", ordersId);
    }

    @Override
    @Transactional
    public void completarPedido(Long ordersId) {
        logger.info("Completing order with ID: {}", ordersId);
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        if (orders.getOrdersEstado() != OrdersEstado.CONFIRMADO) {
            logger.info("Cannot complete order. Current state: {}", orders.getOrdersEstado());
            throw new IllegalStateException("Solo se pueden completar pedidos en estado CONFIRMADO.");
        }

        orders.setOrdersEstado(OrdersEstado.COMPLETADO);
        ordersRepository.save(orders);

        HistorialOrders historial = HistorialOrders.builder()
                .orders(orders)
                .fechaCreacion(LocalDateTime.now())
                .build();
        historialOrdersRepository.save(historial);
        logger.info("Order with ID: {} completed and added to history", ordersId);
    }

    @Override
    public List<OrdersDto> getOrdersByStatuses(List<OrdersEstado> estados) {
        logger.info("Fetching orders by statuses: {}", estados);
        List<Orders> orders = ordersRepository.findByOrdersEstadoIn(estados);
        List<OrdersDto> ordersDtos = orders.stream()
                .map(ordersMapper::toDto)
                .collect(Collectors.toList());
        logger.info("Fetched {} orders by statuses: {}", ordersDtos.size(), estados);
        return ordersDtos;
    }

    @Override
    public OrdersEstado getOrderEstadoById(Long ordersId) {
        logger.info("Fetching order status for order ID: {}", ordersId);
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + ordersId));
        logger.info("Fetched order status: {} for order ID: {}", orders.getOrdersEstado(), ordersId);
        return orders.getOrdersEstado();
    }
}
