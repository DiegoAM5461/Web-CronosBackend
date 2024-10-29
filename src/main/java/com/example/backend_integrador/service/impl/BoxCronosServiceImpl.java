package com.example.backend_integrador.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend_integrador.dto.BoxCronosDto;
import com.example.backend_integrador.entity.BoxCronos;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.repository.BoxCronosRepository;
import com.example.backend_integrador.service.BoxCronosService;
import com.example.backend_integrador.mapper.BoxCronosMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoxCronosServiceImpl implements BoxCronosService {

    private BoxCronosRepository boxCronosRepository;

    @Override
    public BoxCronosDto createBoxCronos(BoxCronosDto boxCronosDto) {
        // Mapear el boxCronos
        BoxCronos boxCronos = BoxCronosMapper.mapToBoxCronos(boxCronosDto);
        BoxCronos savedBoxCronos = boxCronosRepository.save(boxCronos);

        // Retornar el boxCronos guardado como DTO
        return BoxCronosMapper.mapToBoxCronosDto(savedBoxCronos);
    }

    @Override
    public BoxCronosDto getBoxCronosById(Long boxId) {
        BoxCronos boxCronos = boxCronosRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El boxCronos con el id_box: " + boxId + " no existe"));
        return BoxCronosMapper.mapToBoxCronosDto(boxCronos);
    }

    @Override
    public List<BoxCronosDto> getAllBoxCronos() {
        List<BoxCronos> boxCronosList = boxCronosRepository.findAll();
        return boxCronosList.stream().map(BoxCronosMapper::mapToBoxCronosDto)
                .collect(Collectors.toList());
    }

    @Override
    public BoxCronosDto updateBoxCronos(Long boxId, BoxCronosDto updatedBoxCronos) {
        // Buscar el boxCronos existente
        BoxCronos boxCronos = boxCronosRepository.findById(boxId).orElseThrow(
            () -> new ResourceNotFoundException("El boxCronos con id_box: " + boxId + " no existe")
        );

        // Actualizar los atributos del boxCronos
        boxCronos.setBoxNumero(updatedBoxCronos.getBoxNumero());
        boxCronos.setBoxCapacidad(updatedBoxCronos.getBoxCapacidad());
        boxCronos.setBoxEstado(updatedBoxCronos.getBoxEstado());

        BoxCronos updatedBoxCronosObj = boxCronosRepository.save(boxCronos);
        return BoxCronosMapper.mapToBoxCronosDto(updatedBoxCronosObj);
    }

    @Override
    public void deleteBoxCronos(Long boxId) {
        BoxCronos boxCronos = boxCronosRepository.findById(boxId).orElseThrow(
                () -> new ResourceNotFoundException("El boxCronos con el id_box: " + boxId + " no existe"));
        boxCronosRepository.delete(boxCronos);
    }
}