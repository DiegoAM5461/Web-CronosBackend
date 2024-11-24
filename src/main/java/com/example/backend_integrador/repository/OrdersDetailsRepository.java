package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.OrdersDetails;
import com.example.backend_integrador.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersDetailsRepository extends JpaRepository<OrdersDetails, Long> {

    @Query("SELECT od FROM OrdersDetails od WHERE od.orders = :orders AND od.product = :product")
    Optional<OrdersDetails> findByOrdersAndProduct(@Param("orders") Orders orders, @Param("product") Product product);

    List<OrdersDetails> findByOrders(Orders orders);
}
