package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.OrdersDetailsDto;
import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.OrdersDetails;
import com.example.backend_integrador.entity.Product;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.mapper.OrdersDetailsMapper;
import com.example.backend_integrador.repository.OrdersDetailsRepository;
import com.example.backend_integrador.repository.OrdersRepository;
import com.example.backend_integrador.repository.ProductRepository;
import com.example.backend_integrador.service.OrdersDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrdersDetailsServiceImpl implements OrdersDetailsService {

    private OrdersDetailsRepository ordersDetailsRepository;
    private OrdersRepository ordersRepository;
    private ProductRepository productRepository;

    @Override
    public OrdersDetailsDto createOrdersDetails(OrdersDetailsDto dto) {
        Orders orders = ordersRepository.findById(dto.getOrdersId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        OrdersDetails ordersDetails = OrdersDetailsMapper.toEntity(dto);
        ordersDetails.setOrders(orders);
        ordersDetails.setProduct(product);
        OrdersDetails saved = ordersDetailsRepository.save(ordersDetails);
        return OrdersDetailsMapper.toDto(saved);
    }

    @Override
    public OrdersDetailsDto getOrdersDetailsById(Long id) {
        OrdersDetails ordersDetails = ordersDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdersDetails not found"));
        return OrdersDetailsMapper.toDto(ordersDetails);
    }

    @Override
    public List<OrdersDetailsDto> getAllOrdersDetails() {
        return ordersDetailsRepository.findAll().stream()
                .map(OrdersDetailsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrdersDetailsDto updateOrdersDetails(Long id, OrdersDetailsDto updatedDto) {
        OrdersDetails existing = ordersDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdersDetails not found"));

        existing.setQuantity(updatedDto.getQuantity());
        existing.setSubtotal(updatedDto.getSubtotal());
        OrdersDetails updated = ordersDetailsRepository.save(existing);
        return OrdersDetailsMapper.toDto(updated);
    }

    @Override
    public void deleteOrdersDetails(Long id) {
        OrdersDetails ordersDetails = ordersDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdersDetails not found"));
        ordersDetailsRepository.delete(ordersDetails);
    }
}
