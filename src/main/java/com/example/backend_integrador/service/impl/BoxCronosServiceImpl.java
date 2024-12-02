package com.example.backend_integrador.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(BoxCronosServiceImpl.class.getName());
    private BoxCronosRepository boxCronosRepository;

    @Override
    public BoxCronosDto createBoxCronos(BoxCronosDto boxCronosDto) {
        logger.info("Inicio del método createBoxCronos con datos: " + boxCronosDto);
        
        BoxCronos boxCronos = BoxCronosMapper.mapToBoxCronos(boxCronosDto);
        BoxCronos savedBoxCronos = boxCronosRepository.save(boxCronos);

        logger.info("BoxCronos creado con éxito: " + savedBoxCronos);
        return BoxCronosMapper.mapToBoxCronosDto(savedBoxCronos);
    }

    @Override
    public BoxCronosDto getBoxCronosById(Long boxId) {
        logger.info("Inicio del método getBoxCronosById con id: " + boxId);
        
        BoxCronos boxCronos = boxCronosRepository.findById(boxId)
                .orElseThrow(() -> {
                    logger.warning("BoxCronos no encontrado con id: " + boxId);
                    return new ResourceNotFoundException(
                            "El boxCronos con el id_box: " + boxId + " no existe");
                });

        logger.info("BoxCronos encontrado: " + boxCronos);
        return BoxCronosMapper.mapToBoxCronosDto(boxCronos);
    }

    @Override
    public List<BoxCronosDto> getAllBoxCronos() {
        logger.info("Inicio del método getAllBoxCronos");
        
        List<BoxCronos> boxCronosList = boxCronosRepository.findAll();
        logger.info("BoxCronos encontrados: " + boxCronosList.size());

        return boxCronosList.stream().map(BoxCronosMapper::mapToBoxCronosDto)
                .collect(Collectors.toList());
    }

    @Override
    public BoxCronosDto updateBoxCronos(Long boxId, BoxCronosDto updatedBoxCronos) {
        logger.info("Inicio del método updateBoxCronos con id: " + boxId + " y datos: " + updatedBoxCronos);

        BoxCronos boxCronos = boxCronosRepository.findById(boxId).orElseThrow(() -> {
            logger.warning("BoxCronos no encontrado con id: " + boxId);
            return new ResourceNotFoundException("El boxCronos con id_box: " + boxId + " no existe");
        });

        boxCronos.setBoxNumero(updatedBoxCronos.getBoxNumero());
        boxCronos.setBoxCapacidad(updatedBoxCronos.getBoxCapacidad());

        BoxCronos updatedBoxCronosObj = boxCronosRepository.save(boxCronos);
        logger.info("BoxCronos actualizado con éxito: " + updatedBoxCronosObj);

        return BoxCronosMapper.mapToBoxCronosDto(updatedBoxCronosObj);
    }

    @Override
    public void deleteBoxCronos(Long boxId) {
        logger.info("Inicio del método deleteBoxCronos con id: " + boxId);

        BoxCronos boxCronos = boxCronosRepository.findById(boxId).orElseThrow(() -> {
            logger.warning("BoxCronos no encontrado con id: " + boxId);
            return new ResourceNotFoundException("El boxCronos con el id_box: " + boxId + " no existe");
        });

        boxCronosRepository.delete(boxCronos);
        logger.info("BoxCronos eliminado con éxito: " + boxId);
    }
}
