package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.entity.TableCronos;
import com.example.backend_integrador.enums.OrdersDetailsEstado;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class OrdersMapper {

    public Orders toEntity(OrdersDto dto, BoxCronos box, TableCronos tableCronos) {
        if (dto == null) {
            throw new IllegalArgumentException("OrdersDto no puede ser nulo");
        }
        return Orders.builder()
                .ordersId(dto.getOrdersId())
                .box(box)
                .tableCronos(tableCronos)
                .ordersEstado(dto.getOrdersEstado())
                .ordersTotal(dto.getOrdersTotal())
                .build();
    }

    public OrdersDto toDto(Orders entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Orders no puede ser nulo");
        }

        boolean hasPending = entity.getOrdersDetails() != null && entity.getOrdersDetails().stream()
                .anyMatch(detail -> detail.getEstado() == OrdersDetailsEstado.PENDIENTE);

        return OrdersDto.builder()
                .ordersId(entity.getOrdersId())
                .boxId(entity.getBox() != null ? entity.getBox().getBoxId() : null)
                .tableCronosId(entity.getTableCronos() != null ? entity.getTableCronos().getTableCronosId() : null)
                .ordersEstado(entity.getOrdersEstado())
                .ordersTotal(entity.getOrdersTotal())
                .hasPendingProducts(hasPending)
                .details(entity.getOrdersDetails() != null ? entity.getOrdersDetails().stream()
                        .map(OrdersDetailsMapper::toDto)
                        .collect(Collectors.toList())
                        : null) // Mapea los detalles aquí
                .build();
    }

}
