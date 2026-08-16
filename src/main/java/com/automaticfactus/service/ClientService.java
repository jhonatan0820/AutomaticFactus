package com.automaticfactus.service;
import com.automaticfactus.entities.ClientEntity;
import com.automaticfactus.model.SaveResult;
import com.automaticfactus.repositories.ClientRepository;
import com.automaticfactus.dtos.ClientDto;
import com.automaticfactus.dtos.RequestClient;

import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Autocompletado de cliente por tipo + número de identificación.
 */
@Service
public class ClientService {

	private final ClientRepository repo;
    private final TypeIdentificationService types;
    private final DatabaseService db;

    public ClientService(ClientRepository repo, TypeIdentificationService types, DatabaseService db) {
    	this.repo = repo;
        this.types = types;
        this.db = db;
    }

    public Optional<ClientDto> findByTypeAndNumber(String sigla, long numero) {
        if (!db.isReady()) return Optional.empty();
        Optional<Integer> idTipo = types.findIdBySigla(sigla);
        if (idTipo.isEmpty()) return Optional.empty();
        return repo.findByIdTypeIdentificationAndIdentificationNumber(idTipo.get(), numero)
                .map(ClientDto::from);
    }
 

    // Guarda o actualiza un cliente
    public UpsertOutcome upsert(RequestClient req) {
        if (!db.isReady()) return new UpsertOutcome(SaveResult.UNAVAILABLE, null);

        final long identNumber;
        final long cellphone;
        try {
            identNumber = Long.parseLong(req.IdentificationNumber().trim());
            cellphone   = req.Cellphone() == null || req.Cellphone().isBlank()
                    ? 0L : Long.parseLong(req.Cellphone().trim());
        } catch (NumberFormatException e) {
            return new UpsertOutcome(SaveResult.UNAVAILABLE, null);
        }

        Optional<ClientEntity> existing = repo.findByIdTypeIdentificationAndIdentificationNumber(
                req.IdTypeIdentification(), identNumber);

        ClientEntity client = existing.orElseGet(ClientEntity::new);
        client.setName(req.Name());
        client.setIdTypeIdentification(req.IdTypeIdentification());
        client.setIdentificationNumber(identNumber);
        client.setCellphone(cellphone);
        client.setAddress(req.Address());
        client.setEmail(req.email());
        ClientEntity saved = repo.save(client);

        SaveResult result = existing.isPresent() ? SaveResult.UPDATED : SaveResult.CREATED;
        return new UpsertOutcome(result, saved.getIdClient());
    }

    /** Resultado del upsert: estado + id del cliente guardado (útil para el flujo de guardarOrden). */
    public record UpsertOutcome(SaveResult result, Integer idClient) {}

    
}
