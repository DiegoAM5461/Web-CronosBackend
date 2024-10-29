package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.ClientDto;
import com.example.backend_integrador.entity.Client;

public class ClientMapper {
    public static ClientDto mapToClientDto(Client client){
        return new ClientDto(
            client.getClientId(),
            client.getPrimerNombre(),
            client.getPreNombre(),
            client.getPrimerApellido(),
            client.getSegundoApellido(),
            client.getEmail(),
            client.getTelefono()
        );
    }

    public static Client mapToClient(ClientDto clientDto){
        return new Client(
            clientDto.getClientId(),
            clientDto.getPrimerNombre(),
            clientDto.getPreNombre(),
            clientDto.getPrimerApellido(),
            clientDto.getSegundoApellido(),
            clientDto.getEmail(),
            clientDto.getTelefono()
        );
    }
}