package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.TableCronosDto;
import com.example.backend_integrador.entity.TableCronos;

public class TableCronosMapper {

    public static TableCronosDto mapToTableCronosDto(TableCronos tableCronos){
        return new TableCronosDto(
            tableCronos.getTableCronosId(),
            tableCronos.getTableQR(),
            tableCronos.getTableNumero()
        );
    }

    public static TableCronos mapToTableCronos(TableCronosDto tableCronosDto){
        return new TableCronos(
            tableCronosDto.getTableCronosId(),
            tableCronosDto.getTableNumero(),
            tableCronosDto.getTableQR()
        );
    }
}
