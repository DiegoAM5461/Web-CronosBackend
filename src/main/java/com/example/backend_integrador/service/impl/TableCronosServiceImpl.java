package com.example.backend_integrador.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend_integrador.dto.TableCronosDto;
import com.example.backend_integrador.entity.TableCronos;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.mapper.TableCronosMapper;
import com.example.backend_integrador.repository.TableCronosRepository;
import com.example.backend_integrador.service.TableCronosService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TableCronosServiceImpl implements TableCronosService {

    private final TableCronosRepository tableCronosRepository;

    @Override
    public TableCronosDto createTableCronos(TableCronosDto tableCronosDto) {
        TableCronos tableCronos = TableCronosMapper.mapToTableCronos(tableCronosDto);
        TableCronos savedTableCronos = tableCronosRepository.save(tableCronos);
        return TableCronosMapper.mapToTableCronosDto(savedTableCronos);
    }

    @Override
    public TableCronosDto getTableCronosById(Long tableCronosId) {
        TableCronos tableCronos = tableCronosRepository.findById(tableCronosId)
                .orElseThrow(() -> new ResourceNotFoundException("La mesa con el id " + tableCronosId + " no existe"));
        return TableCronosMapper.mapToTableCronosDto(tableCronos);
    }

    @Override
    public List<TableCronosDto> getAllTableCronos() {
        List<TableCronos> tableCronosList = tableCronosRepository.findAll();
        return tableCronosList.stream().map(TableCronosMapper::mapToTableCronosDto)
                .collect(Collectors.toList());
    }

    @Override
    public TableCronosDto upTableCronos(Long tableCronosId, TableCronosDto updatedTableCronos) {
        TableCronos tableCronos = tableCronosRepository.findById(tableCronosId).orElseThrow(
                () -> new ResourceNotFoundException("La mesa con el id " + tableCronosId + " no existe"));

        tableCronos.setTableNumero(updatedTableCronos.getTableNumero());
        tableCronos.setTableQR(updatedTableCronos.getTableQR());

        TableCronos updatedTableCronosObj = tableCronosRepository.save(tableCronos);
        return TableCronosMapper.mapToTableCronosDto(updatedTableCronosObj);
    }

    @Override
    public void deleteTableCronos(Long tableCronosId) {
        TableCronos tableCronos = tableCronosRepository.findById(tableCronosId).orElseThrow(
                () -> new ResourceNotFoundException("La mesa con el id " + tableCronosId + " no existe"));
        tableCronosRepository.delete(tableCronos);
    }
}
