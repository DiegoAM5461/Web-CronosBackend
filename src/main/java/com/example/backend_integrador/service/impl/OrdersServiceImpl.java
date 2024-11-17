package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.entity.TableCronos;
import com.example.backend_integrador.mapper.OrdersMapper;
import com.example.backend_integrador.repository.OrdersRepository;
import com.example.backend_integrador.repository.BoxCronosRepository;
import com.example.backend_integrador.repository.TableCronosRepository;
import com.example.backend_integrador.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private BoxCronosRepository boxCronosRepository;

    @Autowired
    private TableCronosRepository tableCronosRepository;

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public OrdersDto createOrder(OrdersDto ordersDto) {
        BoxCronos box = ordersDto.getBoxId() != null ? boxCronosRepository.findById(ordersDto.getBoxId())
                .orElseThrow(() -> new RuntimeException("Box no encontrado")) : null;
        TableCronos tableCronos = ordersDto.getTableCronosId() != null ? tableCronosRepository.findById(ordersDto.getTableCronosId())
                .orElseThrow(() -> new RuntimeException("Table no encontrada")) : null;

        Orders orders = ordersMapper.toEntity(ordersDto, box, tableCronos);
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
}
