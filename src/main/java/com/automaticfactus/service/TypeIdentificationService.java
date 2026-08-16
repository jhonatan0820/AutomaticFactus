package com.automaticfactus.service;

import com.automaticfactus.entities.TypeIdentificationEntity;
import com.automaticfactus.repositories.TypeIdentificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Catálogo de tipos de identificación (CC, NIT, TI, CE…).
 */
@Service
public class TypeIdentificationService {

    private final TypeIdentificationRepository repo;
    private final DatabaseService db;

    public TypeIdentificationService(TypeIdentificationRepository repo, DatabaseService db) {
        this.repo = repo;
        this.db = db;
    }

    /**
     * Lista los tipos activos (IdState = 1). Si no hay activos, devuelve todos.
     * Si la BD no está lista, devuelve lista vacía sin bloquear.
     */
    public List<TipoDto> listActive() {
        if (!db.isReady()) return List.of();
        try {
            List<TypeIdentificationEntity> tipos = repo.findByIdState(1);
            if (tipos.isEmpty()) tipos = repo.findAll();
            return tipos.stream().map(TipoDto::from).toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public Optional<Integer> findIdBySigla(String sigla) {
        if (!db.isReady()) return Optional.empty();
        return repo.findByTypeIdentification(sigla)
                .map(TypeIdentificationEntity::getIdTypeIdentification);
    }

    public record TipoDto(int id, String TypeIdentification) {
        public static TipoDto from(TypeIdentificationEntity e) {
            return new TipoDto(e.getIdTypeIdentification(), e.getTypeIdentification());
        }
    }
}
