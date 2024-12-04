package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.entity.*;
import com.example.backend_integrador.enums.OrdersDetailsEstado;
import com.example.backend_integrador.enums.OrdersEstado;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.mapper.OrdersDetailsMapper;
import com.example.backend_integrador.repository.*;
import com.example.backend_integrador.service.OrdersDetailsService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrdersDetailsServiceImpl implements OrdersDetailsService {

        private final OrdersDetailsRepository ordersDetailsRepository;
        private final OrdersRepository ordersRepository;
        private final ProductRepository productRepository;
        private final TableCronosRepository tableCronosRepository;
        private final BoxCronosRepository boxCronosRepository;

        @Override
        @Transactional
        public OrdersDetailsDto createOrdersDetails(OrdersDetailsDto dto) {

                Orders orders;

                // Buscar un pedido pendiente asociado al boxId o tableCronosId
                if (dto.getBoxId() != null || dto.getTableCronosId() != null) {
                        BoxCronos box = dto.getBoxId() != null
                                        ? boxCronosRepository.findById(dto.getBoxId())
                                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                                        "Box no encontrado."))
                                        : null;

                        TableCronos table = dto.getTableCronosId() != null
                                        ? tableCronosRepository.findById(dto.getTableCronosId())
                                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                                        "Mesa no encontrada."))
                                        : null;

                        if (box != null) {
                                orders = ordersRepository
                                                .findFirstByBoxAndOrdersEstadoIn(box, List.of(OrdersEstado.PENDIENTE))
                                                .orElseGet(() -> createNewOrder(dto));
                        } else {
                                orders = ordersRepository
                                                .findFirstByTableCronosAndOrdersEstadoIn(table,
                                                                List.of(OrdersEstado.PENDIENTE))
                                                .orElseGet(() -> createNewOrder(dto));
                        }
                } else {
                        throw new IllegalArgumentException("Debe asociar el detalle a un Box o Mesa.");
                }

                // Si el pedido está CANCELADO o COMPLETADO, crear uno nuevo
                if (orders.getOrdersEstado() == OrdersEstado.CANCELADO
                                || orders.getOrdersEstado() == OrdersEstado.COMPLETADO) {
                        orders = createNewOrder(dto);
                }

                // Verificar stock y manejar el detalle
                Product product = productRepository.findById(dto.getProductId())
                                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));
                if (product.getStock() < dto.getQuantity()) {
                        throw new IllegalArgumentException("Stock insuficiente para el producto.");
                }

                OrdersDetails ordersDetails = handleOrderDetail(orders, product, dto);

                // Actualizar stock y guardar
                product.setStock(product.getStock() - dto.getQuantity());
                productRepository.save(product);

                OrdersDetails savedDetails = ordersDetailsRepository.save(ordersDetails);

                // Recalcular total
                recalculateOrderTotal(orders);

                return OrdersDetailsMapper.toDto(savedDetails);
        }

        private Orders createNewOrder(OrdersDetailsDto dto) {
                if (dto.getBoxId() == null && dto.getTableCronosId() == null) {
                        throw new IllegalArgumentException("Debe especificar un Box o una Mesa para crear un pedido.");
                }

                if (dto.getBoxId() != null && dto.getTableCronosId() != null) {
                        throw new IllegalArgumentException("Un pedido no puede estar asociado a ambos: Box y Mesa.");
                }

                BoxCronos box = dto.getBoxId() != null
                                ? boxCronosRepository.findById(dto.getBoxId())
                                                .orElseThrow(() -> new ResourceNotFoundException("Box no encontrado"))
                                : null;

                TableCronos tableCronos = dto.getTableCronosId() != null
                                ? tableCronosRepository.findById(dto.getTableCronosId())
                                                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"))
                                : null;

                Orders newOrder = Orders.builder()
                                .box(box)
                                .tableCronos(tableCronos)
                                .ordersEstado(OrdersEstado.PENDIENTE)
                                .ordersTotal(0.0)
                                .build();

                return ordersRepository.save(newOrder);
        }

        @Override
        public OrdersDetailsDto updateOrdersDetails(Long id, OrdersDetailsDto updatedDto) {
                OrdersDetails existingDetail = ordersDetailsRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("OrdersDetails no encontrado."));

                Orders orders = existingDetail.getOrders();

                // Validar si se pueden modificar productos
                if (!canAddProducts(orders.getOrdersEstado())) {
                        throw new IllegalStateException("No se pueden modificar productos de un pedido en estado "
                                        + orders.getOrdersEstado());
                }

                Product product = productRepository.findById(existingDetail.getProduct().getProductId())
                                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

                // Validar y actualizar cantidad si es diferente
                if (updatedDto.getQuantity() != null
                                && !updatedDto.getQuantity().equals(existingDetail.getQuantity())) {

                        // Nueva validación: si el estado es CONFIRMADO y la cantidad es menor, lanzar
                        // excepción
                        if (existingDetail.getEstado() == OrdersDetailsEstado.CONFIRMADO &&
                                        updatedDto.getQuantity() < existingDetail.getQuantity()) {
                                throw new IllegalStateException(
                                                "No se puede reducir la cantidad de un detalle en estado CONFIRMADO.");
                        }

                        int diferencia = updatedDto.getQuantity() - existingDetail.getQuantity();

                        if (diferencia > 0 && product.getStock() < diferencia) {
                                throw new IllegalArgumentException("Stock insuficiente para el producto.");
                        }

                        product.setStock(product.getStock() - diferencia);
                        productRepository.save(product);

                        existingDetail.setQuantity(updatedDto.getQuantity());
                        existingDetail.setSubtotal(updatedDto.getQuantity() * product.getPrecio());

                        // Cambiar el estado a PENDIENTE si estaba en CONFIRMADO
                        if (existingDetail.getEstado() == OrdersDetailsEstado.CONFIRMADO) {
                                existingDetail.setEstado(OrdersDetailsEstado.PENDIENTE);
                        }
                }

                // Validar y actualizar estado si está presente
                if (updatedDto.getEstado() != null && !updatedDto.getEstado().equals(existingDetail.getEstado())) {
                        existingDetail.setEstado(updatedDto.getEstado());
                }

                OrdersDetails updatedDetail = ordersDetailsRepository.save(existingDetail);

                // Recalcular total si se modificó la cantidad
                recalculateOrderTotal(orders);

                return OrdersDetailsMapper.toDto(updatedDetail);
        }

        @Override
        public void deleteOrdersDetails(Long id) {
                OrdersDetails ordersDetails = ordersDetailsRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("OrdersDetails no encontrado."));

                Orders orders = ordersDetails.getOrders();

                // Eliminar detalle
                ordersDetailsRepository.delete(ordersDetails);

                // Verificar si no quedan más detalles y cambiar estado de la orden a CANCELADO
                if (ordersDetailsRepository.findByOrders(orders).isEmpty()) {
                        orders.setOrdersEstado(OrdersEstado.CANCELADO);
                        ordersRepository.save(orders);
                }

                // Recalcular total
                recalculateOrderTotal(orders);
        }

        @Override
        @Transactional
        public OrdersDetailsDto removeProductFromOrder(Long ordersId, Long productId, Integer quantityToRemove) {
                Orders orders = ordersRepository.findById(ordersId)
                                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado."));

                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

                OrdersDetails existingDetail = ordersDetailsRepository.findByOrdersAndProduct(orders, product)
                                .orElseThrow(() -> new ResourceNotFoundException("El producto no está en el pedido."));

                if (existingDetail.getEstado() == OrdersDetailsEstado.PENDIENTE) {
                        if (existingDetail.getQuantity() > quantityToRemove) {
                                existingDetail.setQuantity(existingDetail.getQuantity() - quantityToRemove);
                                existingDetail.setSubtotal(existingDetail.getQuantity() * product.getPrecio());
                                ordersDetailsRepository.save(existingDetail);
                        } else {
                                ordersDetailsRepository.delete(existingDetail);
                        }

                        product.setStock(product.getStock() + quantityToRemove);
                        productRepository.save(product);
                } else {
                        throw new IllegalStateException("Solo se pueden eliminar productos en estado PENDIENTE.");
                }

                recalculateOrderTotal(orders);

                return OrdersDetailsMapper.toDto(existingDetail);
        }

        private boolean canAddProducts(OrdersEstado estado) {
                return !List.of(OrdersEstado.CANCELADO, OrdersEstado.COMPLETADO).contains(estado);
        }

        private OrdersDetails handleOrderDetail(Orders orders, Product product, OrdersDetailsDto dto) {
                return ordersDetailsRepository.findByOrdersAndProduct(orders, product)
                                .map(existingDetail -> {
                                        if (existingDetail.getEstado() == OrdersDetailsEstado.CONFIRMADO) {
                                                // Crear un nuevo detalle con cantidad acumulada y estado PENDIENTE
                                                return OrdersDetails.builder()
                                                                .orders(orders)
                                                                .product(product)
                                                                .quantity(existingDetail.getQuantity()
                                                                                + dto.getQuantity()) // Sumar cantidad
                                                                                                     // antigua y nueva
                                                                .subtotal((existingDetail.getQuantity()
                                                                                + dto.getQuantity())
                                                                                * product.getPrecio()) // Recalcular
                                                                                                       // subtotal
                                                                .estado(OrdersDetailsEstado.PENDIENTE)
                                                                .build();
                                        } else {
                                                existingDetail.setQuantity(
                                                                existingDetail.getQuantity() + dto.getQuantity());
                                                existingDetail.setSubtotal(
                                                                existingDetail.getQuantity() * product.getPrecio());
                                                return existingDetail;
                                        }
                                })
                                .orElse(OrdersDetails.builder()
                                                .orders(orders)
                                                .product(product)
                                                .quantity(dto.getQuantity())
                                                .subtotal(dto.getQuantity() * product.getPrecio())
                                                .estado(OrdersDetailsEstado.PENDIENTE)
                                                .build());
        }

        private void recalculateOrderTotal(Orders orders) {
                List<OrdersDetails> details = ordersDetailsRepository.findByOrders(orders);
                double total = details.stream().mapToDouble(OrdersDetails::getSubtotal).sum();
                orders.setOrdersTotal(total);

                if (details.isEmpty()) {
                        orders.setOrdersEstado(OrdersEstado.CANCELADO);
                }

                ordersRepository.save(orders);
        }

        @Override
        public List<OrdersDetailsDto> getDetailsByOrder(Long ordersId) {
                Orders orders = ordersRepository.findById(ordersId)
                                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado."));

                List<OrdersDetails> details = ordersDetailsRepository.findByOrders(orders);

                return details.stream()
                                .map(OrdersDetailsMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        public OrdersDetailsDto getOrdersDetailsById(Long id) {
                OrdersDetails ordersDetails = ordersDetailsRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("OrdersDetails no encontrado."));
                return OrdersDetailsMapper.toDto(ordersDetails);
        }

        @Override
        public List<OrdersDetailsDto> getDetailsByBoxAndOrder(Long boxId, Long ordersId) {
                return ordersDetailsRepository.findByBoxIdAndOrdersId(boxId, ordersId)
                                .stream()
                                .map(OrdersDetailsMapper::toDto) // Convertir entidades a DTO
                                .collect(Collectors.toList());
        }

        @Override
        public List<OrdersDetailsDto> getDetailsByTableAndOrder(Long tableCronosId, Long ordersId) {
                return ordersDetailsRepository.findByTableCronosIdAndOrdersId(tableCronosId, ordersId)
                                .stream()
                                .map(OrdersDetailsMapper::toDto) // Convertir entidades a DTO
                                .collect(Collectors.toList());
        }

}
