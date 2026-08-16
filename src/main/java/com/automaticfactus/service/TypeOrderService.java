package com.automaticfactus.service;

import com.automaticfactus.entities.TypeOrderEntity;
import com.automaticfactus.repositories.TypeOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Catálogo de tipos de orden (Cotización, Factura…).
 * Se usa desde el frontend para mostrar solo los activos (IdState = 1) y
 * conocer el {@code IdTypeOrder} correspondiente al enviar a guardarOrden.
 */
@Service
public class TypeOrderService {

    private final TypeOrderRepository repo;
    private final DatabaseService db;

    public TypeOrderService(TypeOrderRepository repo, DatabaseService db) {
        this.repo = repo;
        this.db = db;
    }

    /** Devuelve la lista de tipos activos; vacía si la BD no está lista. */
    public List<TipoOrdenDto> listActive() {
        if (!db.isReady()) return List.of();
        try {
            return repo.findByIdState(1).stream().map(TipoOrdenDto::from).toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public record TipoOrdenDto(int id, String typeOrder) {
        public static TipoOrdenDto from(TypeOrderEntity e) {
            return new TipoOrdenDto(e.getIdTypeOrder(), e.getTypeOrder());
        }
    }
}
