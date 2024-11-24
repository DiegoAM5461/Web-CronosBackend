package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.enums.OrdersEstado;
import com.example.backend_integrador.mapper.OrdersMapper;
import com.example.backend_integrador.repository.OrdersRepository;
import com.example.backend_integrador.service.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrdersMapper ordersMapper;

    @Override
    public OrdersDto createOrder(OrdersDto ordersDto) {
        Orders orders = ordersMapper.toEntity(ordersDto, null, null);
        orders.setOrdersEstado(OrdersEstado.PENDIENTE);
        orders.setOrdersTotal(0.0);
        Orders savedOrder = ordersRepository.save(orders);
        return ordersMapper.toDto(savedOrder);
    }

    @Override
    public OrdersDto getOrderById(Long id) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
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
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        ordersRepository.delete(orders);
    }

    @Override
    public void cancelarPedido(Long ordersId) {
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        if (!orders.getOrdersEstado().equals(OrdersEstado.PENDIENTE)) {
            throw new IllegalStateException("Solo se pueden cancelar pedidos en estado PENDIENTE");
        }
        orders.setOrdersEstado(OrdersEstado.CANCELADO);
        ordersRepository.save(orders);
    }
}
