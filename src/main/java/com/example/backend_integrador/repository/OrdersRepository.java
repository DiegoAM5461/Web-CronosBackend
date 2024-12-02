package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.entity.TableCronos;
import com.example.backend_integrador.enums.OrdersEstado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

        @Query("SELECT o FROM Orders o WHERE o.box = :box AND o.ordersEstado IN :estados")
        Optional<Orders> findByBoxAndOrdersEstadoIn(@Param("box") BoxCronos box,
                        @Param("estados") List<OrdersEstado> estados);

        @Query("SELECT o FROM Orders o WHERE o.tableCronos = :table AND o.ordersEstado IN :estados")
        Optional<Orders> findByTableCronosAndOrdersEstadoIn(@Param("table") TableCronos table,
                        @Param("estados") List<OrdersEstado> estados);

        @Query("SELECT o FROM Orders o WHERE o.ordersEstado = :estado AND o.createdAt < :expiryDate")
        List<Orders> findExpiredOrders(@Param("estado") OrdersEstado estado,
                        @Param("expiryDate") LocalDateTime expiryDate);

        List<Orders> findByBox_BoxId(Long boxId);

        List<Orders> findByTableCronos_TableCronosId(Long tableCronosId);

        boolean existsByBoxAndOrdersEstadoIn(BoxCronos box, List<OrdersEstado> estados);

        boolean existsByTableCronosAndOrdersEstadoIn(TableCronos tableCronos, List<OrdersEstado> estados);

        Optional<Orders> findFirstByBoxAndOrdersEstadoIn(BoxCronos box, List<OrdersEstado> estados);

        Optional<Orders> findFirstByTableCronosAndOrdersEstadoIn(TableCronos tableCronos, List<OrdersEstado> estados);

        @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.ordersDetails WHERE o.ordersId = :ordersId")
        Optional<Orders> findWithDetailsById(@Param("ordersId") Long ordersId);

        @Query("SELECT CASE WHEN COUNT(od) = 0 THEN true ELSE false END "
                        + "FROM OrdersDetails od WHERE od.orders = :orders AND od.estado != 'CONFIRMADO'")
        boolean allProductsConfirmed(@Param("orders") Orders orders);

        List<Orders> findByOrdersEstadoIn(List<OrdersEstado> estados);

}
