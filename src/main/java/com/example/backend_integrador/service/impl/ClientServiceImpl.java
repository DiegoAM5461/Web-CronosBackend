package com.example.backend_integrador.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend_integrador.dto.ClientDto;
import com.example.backend_integrador.entity.Client;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.repository.ClientRepository;
import com.example.backend_integrador.service.ClientService;
import com.example.backend_integrador.mapper.ClientMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        // Mapear el cliente
        Client client = ClientMapper.mapToClient(clientDto);
        Client savedClient = clientRepository.save(client);

        // Retornar el cliente guardado como DTO
        return ClientMapper.mapToClientDto(savedClient);
    }

    @Override
    public ClientDto getClientById(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El cliente con el id_client: " + clientId + " no existe"));
        return ClientMapper.mapToClientDto(client);
    }

    @Override
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(ClientMapper::mapToClientDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto updateClient(Long clientId, ClientDto updatedClient) {
        // Buscar el cliente existente
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new ResourceNotFoundException("El cliente con id_client: " + clientId + " no existe"));

        // Actualizar los atributos del cliente
        client.setPrimerNombre(updatedClient.getPrimerNombre());
        client.setPreNombre(updatedClient.getPreNombre());
        client.setPrimerApellido(updatedClient.getPrimerApellido());
        client.setSegundoApellido(updatedClient.getSegundoApellido());
        client.setEmail(updatedClient.getEmail());
        client.setTelefono(updatedClient.getTelefono());

        Client updatedClientObj = clientRepository.save(client);
        return ClientMapper.mapToClientDto(updatedClientObj);
    }

    @Override
    public ClientDto updateClientId(Long currentClientId, Long newClientId) {
        // Buscar el cliente existente
        Client client = clientRepository.findById(currentClientId).orElseThrow(
                () -> new ResourceNotFoundException("El cliente con id_client: " + currentClientId + " no existe"));

        // Actualizar el clientId
        client.setClientId(newClientId);

        Client updatedClient = clientRepository.save(client);
        return ClientMapper.mapToClientDto(updatedClient);
    }

    @Override
    public void deleteClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new ResourceNotFoundException("El cliente con el id_client: " + clientId + " no existe"));
        clientRepository.delete(client);
    }
}
