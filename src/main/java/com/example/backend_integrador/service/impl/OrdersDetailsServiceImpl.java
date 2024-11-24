package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.OrdersDetails;
import com.example.backend_integrador.entity.Product;
import com.example.backend_integrador.enums.OrdersEstado;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.mapper.OrdersDetailsMapper;
import com.example.backend_integrador.repository.BoxCronosRepository;
import com.example.backend_integrador.repository.OrdersDetailsRepository;
import com.example.backend_integrador.repository.OrdersRepository;
import com.example.backend_integrador.repository.ProductRepository;
import com.example.backend_integrador.repository.TableCronosRepository;
import com.example.backend_integrador.service.OrdersDetailsService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrdersDetailsServiceImpl implements OrdersDetailsService {

        private final OrdersDetailsRepository ordersDetailsRepository;
        private final OrdersRepository ordersRepository;
        private final ProductRepository productRepository;
        private final BoxCronosRepository boxCronosRepository;
        private final TableCronosRepository tableCronosRepository;

        @Override
        @Transactional
        public OrdersDetailsDto createOrdersDetails(OrdersDetailsDto dto) {
                // Buscar o crear un pedido en estado PENDIENTE
                Orders orders = findOrCreatePendingOrder(dto);

                // Verificar si el producto existe
                Product product = productRepository.findById(dto.getProductId())
                                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

                // Validar stock disponible
                if (product.getStock() < dto.getQuantity()) {
                        throw new IllegalArgumentException(
                                        "Stock insuficiente para el producto: " + product.getNombre());
                }

                // Reservar stock
                product.setStock(product.getStock() - dto.getQuantity());
                productRepository.save(product);

                // Crear o actualizar el detalle del pedido
                OrdersDetails ordersDetails = ordersDetailsRepository.findByOrdersAndProduct(orders, product)
                                .map(existingDetail -> {
                                        existingDetail.setQuantity(existingDetail.getQuantity() + dto.getQuantity());
                                        existingDetail.setSubtotal(existingDetail.getQuantity() * product.getPrecio());
                                        return existingDetail;
                                })
                                .orElse(OrdersDetails.builder()
                                                .orders(orders)
                                                .product(product)
                                                .quantity(dto.getQuantity())
                                                .subtotal(dto.getQuantity() * product.getPrecio())
                                                .build());

                OrdersDetails savedDetail = ordersDetailsRepository.save(ordersDetails);

                // Recalcular el total del pedido
                recalculateOrderTotal(orders);

                return OrdersDetailsMapper.toDto(savedDetail);
        }

        @Override
        @Transactional
        public OrdersDetailsDto removeProductFromOrder(Long ordersId, Long productId, Integer quantityToRemove) {
                Orders orders = ordersRepository.findById(ordersId)
                                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

                OrdersDetails existingDetail = ordersDetailsRepository.findByOrdersAndProduct(orders, product)
                                .orElseThrow(() -> new ResourceNotFoundException("El producto no está en el pedido"));

                // Reducir cantidad o eliminar detalle si la cantidad llega a 0
                if (existingDetail.getQuantity() > quantityToRemove) {
                        existingDetail.setQuantity(existingDetail.getQuantity() - quantityToRemove);
                        existingDetail.setSubtotal(existingDetail.getQuantity() * product.getPrecio());
                        ordersDetailsRepository.save(existingDetail);
                } else {
                        ordersDetailsRepository.delete(existingDetail);
                }

                // Liberar stock
                product.setStock(product.getStock() + quantityToRemove);
                productRepository.save(product);

                // Recalcular el total del pedido
                recalculateOrderTotal(orders);

                // Eliminar el pedido si no quedan detalles
                if (ordersDetailsRepository.findByOrders(orders).isEmpty()) {
                        ordersRepository.delete(orders);
                }

                return OrdersDetailsMapper.toDto(existingDetail);
        }

        private Orders findOrCreatePendingOrder(OrdersDetailsDto dto) {
                Optional<Orders> optionalOrder = dto.getBoxId() != null
                                ? ordersRepository.findByBoxAndOrdersEstado(
                                                boxCronosRepository.findById(dto.getBoxId())
                                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                                "Box no encontrado")),
                                                OrdersEstado.PENDIENTE)
                                : ordersRepository.findByTableCronosAndOrdersEstado(
                                                tableCronosRepository.findById(dto.getTableCronosId())
                                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                                "Mesa no encontrada")),
                                                OrdersEstado.PENDIENTE);

                return optionalOrder.orElseGet(() -> {
                        Orders newOrder = Orders.builder()
                                        .box(dto.getBoxId() != null
                                                        ? boxCronosRepository.findById(dto.getBoxId()).orElse(null)
                                                        : null)
                                        .tableCronos(dto.getTableCronosId() != null
                                                        ? tableCronosRepository.findById(dto.getTableCronosId())
                                                                        .orElse(null)
                                                        : null)
                                        .ordersEstado(OrdersEstado.PENDIENTE)
                                        .ordersTotal(0.0)
                                        .build();
                        return ordersRepository.save(newOrder);
                });
        }

        @Override
        public OrdersDetailsDto getOrdersDetailsById(Long id) {
                OrdersDetails ordersDetails = ordersDetailsRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("OrdersDetails not found"));
                return OrdersDetailsMapper.toDto(ordersDetails);
        }

        @Override
        public OrdersDetailsDto updateOrdersDetails(Long id, OrdersDetailsDto updatedDto) {
                OrdersDetails existingDetail = ordersDetailsRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("OrdersDetails not found"));

                if (updatedDto.getQuantity() == null || updatedDto.getQuantity() <= 0) {
                        throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
                }

                Product product = productRepository.findById(existingDetail.getProduct().getProductId())
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

                existingDetail.setQuantity(updatedDto.getQuantity());
                existingDetail.setSubtotal(updatedDto.getQuantity() * product.getPrecio());

                OrdersDetails updatedDetail = ordersDetailsRepository.save(existingDetail);

                recalculateOrderTotal(existingDetail.getOrders());

                return OrdersDetailsMapper.toDto(updatedDetail);
        }

        @Override
        public void deleteOrdersDetails(Long id) {
                OrdersDetails ordersDetails = ordersDetailsRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("OrdersDetails not found"));
                ordersDetailsRepository.delete(ordersDetails);
        }

        private void recalculateOrderTotal(Orders orders) {
                List<OrdersDetails> details = ordersDetailsRepository.findByOrders(orders);
                double total = details.stream().mapToDouble(OrdersDetails::getSubtotal).sum();
                orders.setOrdersTotal(total);
                ordersRepository.save(orders);
        }

        @Override
        public List<OrdersDetailsDto> getDetailsByOrder(Long ordersId) {
                Orders orders = ordersRepository.findById(ordersId)
                                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

                List<OrdersDetails> details = ordersDetailsRepository.findByOrders(orders);

                return details.stream()
                                .map(OrdersDetailsMapper::toDto)
                                .collect(Collectors.toList());
        }

}
