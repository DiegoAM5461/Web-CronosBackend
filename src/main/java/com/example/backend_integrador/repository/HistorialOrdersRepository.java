package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.HistorialOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface HistorialOrdersRepository extends JpaRepository<HistorialOrders, Long> {
        List<HistorialOrders> findByFechaCreacionBetween(LocalDateTime start, LocalDateTime end);

        @Query(value = "SELECT DATE(fecha_creacion) AS fecha, COUNT(*) AS total_ordenes " +
                        "FROM historial_orders " +
                        "WHERE fecha_creacion BETWEEN :start AND :end " +
                        "GROUP BY DATE(fecha_creacion) " +
                        "ORDER BY fecha ASC", nativeQuery = true)
        List<Map<String, Object>> findDailyAnalysis(@Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

        @Query(value = "SELECT h.fecha_creacion AS fecha, " +
                        "o.orders_id AS ordersId, " +
                        "o.orders_estado AS estado, " +
                        "o.box_id AS boxId, " +
                        "o.table_cronos_id AS tableCronosId, " +
                        "p.nombre_product AS productName, " +
                        "d.cantidad_detalle AS quantity, " +
                        "d.subtotal AS price " +
                        "FROM historial_orders h " +
                        "JOIN orders o ON h.orders_id = o.orders_id " +
                        "JOIN detalle_pedido d ON o.orders_id = d.id_pedido " +
                        "JOIN product p ON d.id_producto = p.product_id " +
                        "WHERE h.fecha_creacion BETWEEN :start AND :end " +
                        "ORDER BY h.fecha_creacion ASC", nativeQuery = true)
        List<Map<String, Object>> findDetailedOrders(@Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

}
