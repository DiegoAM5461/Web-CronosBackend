package com.example.backend_integrador.controller;

import java.util.List;

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

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private ClientService clientService;

    // Create a new client REST API
    @PostMapping
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        ClientDto savedClient = clientService.createClient(clientDto);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    // Get a client by ID REST API
    @GetMapping("{clientId}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("clientId") Long clientId) {
        ClientDto clientDto = clientService.getClientById(clientId);
        return ResponseEntity.ok(clientDto);
    }

    // Get all clients REST API
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // Update a client by ID REST API
    @PutMapping("{clientId}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable("clientId") Long clientId,
            @RequestBody ClientDto updatedClient) {
        ClientDto clientDto = clientService.updateClient(clientId, updatedClient);
        return ResponseEntity.ok(clientDto);
    }

    @PutMapping("{currentClientId}/updateId/{newClientId}")
    public ResponseEntity<ClientDto> updateClientId(@PathVariable("currentClientId") Long currentClientId,
            @PathVariable("newClientId") Long newClientId) {
        ClientDto clientDto = clientService.updateClientId(currentClientId, newClientId);
        return ResponseEntity.ok(clientDto);
    }

    // Delete a client by ID REST API
    @DeleteMapping("{clientId}")
    public ResponseEntity<String> deleteClient(@PathVariable("clientId") Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok("Cliente eliminado correctamente");
    }
}
