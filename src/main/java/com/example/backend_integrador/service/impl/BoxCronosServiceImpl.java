package com.example.backend_integrador.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(BoxCronosServiceImpl.class);

    private final BoxCronosRepository boxCronosRepository;

    @Override
    public BoxCronosDto createBoxCronos(BoxCronosDto boxCronosDto) {
        logger.info("Creando un nuevo BoxCronos");
        logger.debug("Datos del BoxCronos a crear: {}", boxCronosDto);

        BoxCronos boxCronos = BoxCronosMapper.mapToBoxCronos(boxCronosDto);
        BoxCronos savedBoxCronos = boxCronosRepository.save(boxCronos);

        logger.info("BoxCronos creado con éxito: {}", savedBoxCronos);
        return BoxCronosMapper.mapToBoxCronosDto(savedBoxCronos);
    }

    @Override
    public BoxCronosDto getBoxCronosById(Long boxId) {
        logger.info("Obteniendo BoxCronos por ID: {}", boxId);

        BoxCronos boxCronos = boxCronosRepository.findById(boxId)
                .orElseThrow(() -> {
                    logger.error("BoxCronos con ID {} no encontrado", boxId);
                    return new ResourceNotFoundException("El BoxCronos con el ID: " + boxId + " no existe");
                });

        logger.debug("BoxCronos encontrado: {}", boxCronos);
        return BoxCronosMapper.mapToBoxCronosDto(boxCronos);
    }

    @Override
    public List<BoxCronosDto> getAllBoxCronos() {
        logger.info("Obteniendo todos los BoxCronos");

        List<BoxCronos> boxCronosList = boxCronosRepository.findAll();

        logger.debug("Cantidad de BoxCronos encontrados: {}", boxCronosList.size());
        return boxCronosList.stream()
                .map(BoxCronosMapper::mapToBoxCronosDto)
                .collect(Collectors.toList());
    }

    @Override
    public BoxCronosDto updateBoxCronos(Long boxId, BoxCronosDto updatedBoxCronos) {
        logger.info("Actualizando BoxCronos con ID: {}", boxId);

        BoxCronos boxCronos = boxCronosRepository.findById(boxId)
                .orElseThrow(() -> {
                    logger.error("BoxCronos con ID {} no encontrado para actualizar", boxId);
                    return new ResourceNotFoundException("El BoxCronos con el ID: " + boxId + " no existe");
                });

        logger.debug("Datos actuales del BoxCronos: {}", boxCronos);
        boxCronos.setBoxNumero(updatedBoxCronos.getBoxNumero());
        boxCronos.setBoxCapacidad(updatedBoxCronos.getBoxCapacidad());

        BoxCronos updatedBoxCronosObj = boxCronosRepository.save(boxCronos);
        logger.info("BoxCronos actualizado con éxito: {}", updatedBoxCronosObj);

        return BoxCronosMapper.mapToBoxCronosDto(updatedBoxCronosObj);
    }

    @Override
    public void deleteBoxCronos(Long boxId) {
        logger.info("Eliminando BoxCronos con ID: {}", boxId);

        BoxCronos boxCronos = boxCronosRepository.findById(boxId)
                .orElseThrow(() -> {
                    logger.error("BoxCronos con ID {} no encontrado para eliminar", boxId);
                    return new ResourceNotFoundException("El BoxCronos con el ID: " + boxId + " no existe");
                });

        boxCronosRepository.delete(boxCronos);
        logger.info("BoxCronos con ID {} eliminado con éxito", boxId);
    }
}
