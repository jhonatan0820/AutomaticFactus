package com.automaticfactus.controller;

import com.automaticfactus.service.ClientService;

import Dtos.ClientDto;
import Dtos.RequestClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    /** GET /api/clientes/buscar?sigla=CC&numero=1234 */
    @GetMapping("/buscar")
    public ResponseEntity<?> search(@RequestParam String sigla, @RequestParam String numero) {
        if (sigla.isBlank() || numero.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Parámetros requeridos: sigla, numero"));
        }
        long num;
        try {
            num = Long.parseLong(numero.trim());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "numero inválido"));
        }

        try {
            Optional<ClientDto> found = service.findByTypeAndNumber(sigla.trim(), num);
            if (found.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("found", false));
            }
            return ResponseEntity.ok(found.get());
        } catch (Exception e) {
            System.err.println("[/api/clientes/buscar] "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
            return ResponseEntity.ok(Map.of("found", false));
        }
    }
    
    @PostMapping("/guardarCliente")
    public ResponseEntity<?> saveClient(@RequestBody RequestClient requestClient) {
        if (requestClient == null
                || requestClient.Name() == null || requestClient.Name().isBlank()
                || requestClient.IdTypeIdentification() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            ClientService.UpsertOutcome outcome = service.upsert(requestClient);
            Map<String, Object> body = Map.of("idClient", outcome.idClient());
            return switch (outcome.result()) {
                case CREATED     -> ResponseEntity.status(201).body(body);
                case UPDATED     -> ResponseEntity.ok(body);
                case UNAVAILABLE -> ResponseEntity.status(503).build();
            };
        } catch (Exception e) {
            System.err.println("[/api/clientes/guardarCliente] "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
    
    
}
