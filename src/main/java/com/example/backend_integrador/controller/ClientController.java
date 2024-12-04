package com.example.backend_integrador.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_integrador.dto.ClientDto;
import com.example.backend_integrador.service.ClientService;

import lombok.AllArgsConstructor;

@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;

    // Create a new client REST API
    @PostMapping
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        logger.info("POST /api/clients - Creando un nuevo cliente");
        logger.debug("Datos del cliente a crear: {}", clientDto);
        ClientDto savedClient = clientService.createClient(clientDto);
        logger.info("Cliente creado con éxito: {}", savedClient);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    // Get a client by ID REST API
    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("clientId") Long clientId) {
        logger.info("GET /api/clients/{} - Obtener cliente por ID", clientId);
        ClientDto clientDto = clientService.getClientById(clientId);
        if (clientDto != null) {
            logger.debug("Cliente encontrado: {}", clientDto);
        } else {
            logger.warn("No se encontró un cliente con ID: {}", clientId);
        }
        return ResponseEntity.ok(clientDto);
    }

    // Get all clients REST API
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        logger.info("GET /api/clients - Obtener todos los clientes");
        List<ClientDto> clients = clientService.getAllClients();
        logger.debug("Cantidad de clientes encontrados: {}", clients.size());
        return ResponseEntity.ok(clients);
    }

    // Update a client by ID REST API
    @PutMapping("/{clientId}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable("clientId") Long clientId,
                                                  @RequestBody ClientDto updatedClient) {
        logger.info("PUT /api/clients/{} - Actualizando cliente", clientId);
        logger.debug("Datos para actualizar: {}", updatedClient);
        ClientDto clientDto = clientService.updateClient(clientId, updatedClient);
        logger.info("Cliente actualizado con éxito: {}", clientDto);
        return ResponseEntity.ok(clientDto);
    }

    // Update a client's ID REST API
    @PutMapping("/{currentClientId}/updateId/{newClientId}")
    public ResponseEntity<ClientDto> updateClientId(@PathVariable("currentClientId") Long currentClientId,
                                                    @PathVariable("newClientId") Long newClientId) {
        logger.info("PUT /api/clients/{}/updateId/{} - Actualizando ID del cliente", currentClientId, newClientId);
        ClientDto clientDto = clientService.updateClientId(currentClientId, newClientId);
        logger.info("ID del cliente actualizado con éxito. Cliente resultante: {}", clientDto);
        return ResponseEntity.ok(clientDto);
    }

    // Delete a client by ID REST API
    @DeleteMapping("/{clientId}")
    public ResponseEntity<String> deleteClient(@PathVariable("clientId") Long clientId) {
        logger.info("DELETE /api/clients/{} - Eliminando cliente", clientId);
        clientService.deleteClient(clientId);
        logger.info("Cliente con ID {} eliminado correctamente", clientId);
        return ResponseEntity.ok("Cliente eliminado correctamente");
    }
}
