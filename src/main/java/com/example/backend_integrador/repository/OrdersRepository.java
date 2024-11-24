package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.entity.TableCronos;
import com.example.backend_integrador.enums.OrdersEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o WHERE o.box = :box AND o.ordersEstado = :estado")
    Optional<Orders> findByBoxAndOrdersEstado(@Param("box") BoxCronos box, @Param("estado") OrdersEstado estado);

    @Query("SELECT o FROM Orders o WHERE o.tableCronos = :table AND o.ordersEstado = :estado")
    Optional<Orders> findByTableCronosAndOrdersEstado(@Param("table") TableCronos table, @Param("estado") OrdersEstado estado);
}
