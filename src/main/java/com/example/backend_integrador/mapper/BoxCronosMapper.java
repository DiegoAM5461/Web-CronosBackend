package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.BoxCronosDto;
import com.example.backend_integrador.entity.BoxCronos;

public class BoxCronosMapper {
    public static BoxCronosDto mapToBoxCronosDto(BoxCronos boxCronos){
        return new BoxCronosDto(
            boxCronos.getBoxId(),
            boxCronos.getBoxNumero(),
            boxCronos.getBoxCapacidad(),
            boxCronos.getBoxEstado()
        );
    }

    public static BoxCronos mapToBoxCronos(BoxCronosDto boxCronosDto){
        return new BoxCronos(
            boxCronosDto.getBoxId(),
            boxCronosDto.getBoxNumero(),
            boxCronosDto.getBoxCapacidad(),
            boxCronosDto.getBoxEstado()
        );
    }
}