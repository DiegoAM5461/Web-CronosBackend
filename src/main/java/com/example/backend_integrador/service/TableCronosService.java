package com.example.backend_integrador.service;

import java.util.List;

import com.example.backend_integrador.dto.TableCronosDto;

public interface TableCronosService {

    TableCronosDto createTableCronos(TableCronosDto tableCronosDto);

    TableCronosDto getTableCronosById(Long tableCronosId);

    List<TableCronosDto> getAllTableCronos();

    TableCronosDto upTableCronos(Long tableCronosId, TableCronosDto updatedTableCronos );

    void deleteTableCronos(Long tableCronosId);
}

