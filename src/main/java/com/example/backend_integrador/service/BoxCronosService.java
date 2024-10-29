package com.example.backend_integrador.service;

import java.util.List;
import com.example.backend_integrador.dto.BoxCronosDto;

public interface BoxCronosService {
    BoxCronosDto createBoxCronos(BoxCronosDto boxCronosDto);

    BoxCronosDto getBoxCronosById(Long boxId);

    List<BoxCronosDto> getAllBoxCronos();

    BoxCronosDto updateBoxCronos(Long boxId, BoxCronosDto updatedBoxCronos);

    void deleteBoxCronos(Long boxId);
}