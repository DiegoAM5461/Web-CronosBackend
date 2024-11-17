package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.OrdersDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersDetailsRepository extends JpaRepository<OrdersDetails, Long> {
}
