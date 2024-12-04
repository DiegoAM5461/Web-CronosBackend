package com.example.backend_integrador.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        logger.info("Creando un nuevo cliente");
        logger.debug("Datos del cliente a crear: {}", clientDto);

        Client client = ClientMapper.mapToClient(clientDto);
        Client savedClient = clientRepository.save(client);

        logger.info("Cliente creado con éxito: {}", savedClient);
        return ClientMapper.mapToClientDto(savedClient);
    }

    @Override
    public ClientDto getClientById(Long clientId) {
        logger.info("Obteniendo cliente por ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Cliente con ID {} no encontrado", clientId);
                    return new ResourceNotFoundException("El cliente con el ID: " + clientId + " no existe");
                });

        logger.debug("Cliente encontrado: {}", client);
        return ClientMapper.mapToClientDto(client);
    }

    @Override
    public List<ClientDto> getAllClients() {
        logger.info("Obteniendo todos los clientes");

        List<Client> clients = clientRepository.findAll();
        logger.debug("Cantidad de clientes encontrados: {}", clients.size());

        return clients.stream()
                .map(ClientMapper::mapToClientDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto updateClient(Long clientId, ClientDto updatedClient) {
        logger.info("Actualizando cliente con ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Cliente con ID {} no encontrado para actualizar", clientId);
                    return new ResourceNotFoundException("El cliente con el ID: " + clientId + " no existe");
                });

        logger.debug("Datos actuales del cliente: {}", client);
        client.setPrimerNombre(updatedClient.getPrimerNombre());
        client.setPrimerApellido(updatedClient.getPrimerApellido());
        client.setSegundoApellido(updatedClient.getSegundoApellido());
        client.setEmail(updatedClient.getEmail());
        client.setTelefono(updatedClient.getTelefono());

        Client updatedClientObj = clientRepository.save(client);

        logger.info("Cliente actualizado con éxito: {}", updatedClientObj);
        return ClientMapper.mapToClientDto(updatedClientObj);
    }

    @Override
    public ClientDto updateClientId(Long currentClientId, Long newClientId) {
        logger.info("Actualizando ID del cliente con ID actual: {} a nuevo ID: {}", currentClientId, newClientId);

        Client client = clientRepository.findById(currentClientId)
                .orElseThrow(() -> {
                    logger.error("Cliente con ID actual {} no encontrado para actualizar", currentClientId);
                    return new ResourceNotFoundException("El cliente con el ID actual: " + currentClientId + " no existe");
                });

        client.setClientId(newClientId);

        Client updatedClient = clientRepository.save(client);

        logger.info("ID del cliente actualizado con éxito: {}", updatedClient);
        return ClientMapper.mapToClientDto(updatedClient);
    }

    @Override
    public void deleteClient(Long clientId) {
        logger.info("Eliminando cliente con ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Cliente con ID {} no encontrado para eliminar", clientId);
                    return new ResourceNotFoundException("El cliente con el ID: " + clientId + " no existe");
                });

        clientRepository.delete(client);
        logger.info("Cliente con ID {} eliminado con éxito", clientId);
    }
}
