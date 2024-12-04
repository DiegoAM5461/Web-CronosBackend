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
        // Validar que no esté asociado a ambos (Box y Mesa)
        if (ordersDto.getBoxId() != null && ordersDto.getTableCronosId() != null) {
            throw new IllegalArgumentException("Un pedido no puede estar asociado a ambos: Box y Mesa.");
        }

        // Validar que esté asociado a uno de ellos
        if (ordersDto.getBoxId() == null && ordersDto.getTableCronosId() == null) {
            throw new IllegalArgumentException("Debe asociar un Box o una Mesa al pedido.");
        }

        // Recuperar entidades asociadas
        BoxCronos box = null;
        TableCronos tableCronos = null;

        // Verificar restricciones para BoxCronos
        if (ordersDto.getBoxId() != null) {
            box = boxCronosRepository.findById(ordersDto.getBoxId())
                    .orElseThrow(() -> new ResourceNotFoundException("El Box no existe."));
            if (ordersRepository.existsByBoxAndOrdersEstadoIn(box,
                    List.of(
                            OrdersEstado.PENDIENTE,
                            OrdersEstado.CONFIRMADO))) {
                throw new IllegalStateException("El Box ya tiene un pedido activo.");
            }
        }

        // Verificar restricciones para TableCronos
        if (ordersDto.getTableCronosId() != null) {
            tableCronos = tableCronosRepository.findById(ordersDto.getTableCronosId())
                    .orElseThrow(() -> new ResourceNotFoundException("La Mesa no existe."));
            if (ordersRepository.existsByTableCronosAndOrdersEstadoIn(tableCronos,
                    List.of(OrdersEstado.PENDIENTE,
                            OrdersEstado.CONFIRMADO))) {
                throw new IllegalStateException("La Mesa ya tiene un pedido activo.");
            }
        }

        // Crear el pedido
        Orders orders = ordersMapper.toEntity(ordersDto, box, tableCronos);
        orders.setOrdersEstado(OrdersEstado.PENDIENTE);
        orders.setOrdersTotal(0.0);

        Orders savedOrder = ordersRepository.save(orders);
        return ordersMapper.toDto(savedOrder);
    }

    @Override
    public OrdersDto getOrderById(Long id) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
        return ordersMapper.toDto(orders);
    }

    @Override
    public List<OrdersDto> getAllOrders() {
        return ordersRepository.findAll().stream()
                .map(ordersMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
        ordersRepository.delete(orders);
    }

    @Override
    @Transactional
    public void cancelarPedido(Long ordersId) {
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> {
                    logger.error("Pedido no encontrado con ID: {}", ordersId);
                    return new ResourceNotFoundException("Pedido no encontrado con ID: " + ordersId);
                });

        if (!orders.getOrdersEstado().equals(OrdersEstado.PENDIENTE)) {
            logger.error("No se puede cancelar el pedido con estado: {}", orders.getOrdersEstado());
            throw new IllegalStateException("Solo se pueden cancelar pedidos en estado PENDIENTE.");
        }

        // Liberar recursos como stock
        ordersDetailsRepository.findByOrders(orders).forEach(detail -> {
            Product product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
            productRepository.save(product);
        });

        orders.setOrdersEstado(OrdersEstado.CANCELADO);
        ordersRepository.save(orders);

        logger.info("Pedido con ID {} cancelado exitosamente.", ordersId);
    }

    @Override
    @Transactional
    public void autoCancelOrCompleteExpiredOrders() {
        LocalDateTime expiryDate = LocalDateTime.now().minusHours(24);
        List<Orders> expiredOrders = ordersRepository.findExpiredOrders(OrdersEstado.PENDIENTE, expiryDate);

        for (Orders orders : expiredOrders) {
            // Liberar recursos (stock o mesa)
            List<OrdersDetails> orderDetails = ordersDetailsRepository.findByOrders(orders);
            for (OrdersDetails detail : orderDetails) {
                Product product = detail.getProduct();
                product.setStock(product.getStock() + detail.getQuantity());
                productRepository.save(product);
            }

            // Cambiar estado a CANCELADO
            orders.setOrdersEstado(OrdersEstado.CANCELADO);
            ordersRepository.save(orders);
        }
    }

    @Override
    public List<OrdersDto> getOrdersByBoxId(Long boxId) {
        return ordersRepository.findByBox_BoxId(boxId)
                .stream()
                .map(order -> ordersMapper.toDto(order))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdersDto> getOrdersByTableCronosId(Long tableCronosId) {
        return ordersRepository.findByTableCronos_TableCronosId(tableCronosId)
                .stream()
                .map(order -> ordersMapper.toDto(order))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void confirmarPedido(Long ordersId) {
        // Buscar el pedido
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        // Validar si el pedido puede ser confirmado
        if (orders.getOrdersEstado() != OrdersEstado.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden confirmar pedidos en estado PENDIENTE.");
        }

        // Verificar que todos los productos estén en estado CONFIRMADO
        List<OrdersDetails> details = ordersDetailsRepository.findByOrders(orders);
        boolean allConfirmed = details.stream()
                .allMatch(detail -> detail.getEstado() == OrdersDetailsEstado.CONFIRMADO);

        if (!allConfirmed) {
            throw new IllegalStateException(
                    "No se puede confirmar un pedido con productos en estado diferente a CONFIRMADO.");
        }

        // Cambiar estado del pedido a CONFIRMADO
        orders.setOrdersEstado(OrdersEstado.CONFIRMADO);
        ordersRepository.save(orders);
    }

    @Override
    @Transactional
    public void completarPedido(Long ordersId) {
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        if (orders.getOrdersEstado() != OrdersEstado.CONFIRMADO) {
            throw new IllegalStateException("Solo se pueden completar pedidos en estado CONFIRMADO.");
        }

        // Cambiar el estado a COMPLETADO
        orders.setOrdersEstado(OrdersEstado.COMPLETADO);
        ordersRepository.save(orders);

        // Crear registro en HistorialOrders
        HistorialOrders historial = HistorialOrders.builder()
                .orders(orders)
                .fechaCreacion(LocalDateTime.now())
                .build();
        historialOrdersRepository.save(historial);
    }

    @Override
    public List<OrdersDto> getOrdersByStatuses(List<OrdersEstado> estados) {
        List<Orders> orders = ordersRepository.findByOrdersEstadoIn(estados);
        return orders.stream()
                .map(ordersMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrdersEstado getOrderEstadoById(Long ordersId) {
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + ordersId));
        return orders.getOrdersEstado();
    }

}
