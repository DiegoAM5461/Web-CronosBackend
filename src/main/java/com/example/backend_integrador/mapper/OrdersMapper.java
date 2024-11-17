package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.OrdersDto;
import com.example.backend_integrador.entity.Orders;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.entity.TableCronos;
import org.springframework.stereotype.Component;

@Component
public class OrdersMapper {

    public Orders toEntity(OrdersDto dto, BoxCronos box, TableCronos tableCronos) {
        return Orders.builder()
                .ordersId(dto.getOrdersId())
                .box(box)
                .tableCronos(tableCronos)
                .ordersEstado(dto.getOrdersEstado())
                .ordersTotal(dto.getOrdersTotal())
                .build();
    }

    public OrdersDto toDto(Orders entity) {
        return OrdersDto.builder()
                .ordersId(entity.getOrdersId())
                .boxId(entity.getBox() != null ? entity.getBox().getBoxId() : null)
                .tableCronosId(entity.getTableCronos() != null ? entity.getTableCronos().getTableCronosId() : null)
                .ordersEstado(entity.getOrdersEstado())
                .ordersTotal(entity.getOrdersTotal())
                .build();
    }
}
