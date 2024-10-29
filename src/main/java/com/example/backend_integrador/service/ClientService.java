package com.example.backend_integrador.service;

import java.util.List;
import com.example.backend_integrador.dto.ClientDto;

public interface ClientService {
    ClientDto createClient(ClientDto clientDto);

    ClientDto getClientById(Long clientId);

    List<ClientDto> getAllClients();

    ClientDto updateClient(Long clientId, ClientDto updatedClient);

    ClientDto updateClientId(Long currentClientId, Long newClientId);

    void deleteClient(Long clientId);
}