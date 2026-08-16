package com.automaticfactus.repositories;

import com.automaticfactus.entities.TypeIdentificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TypeIdentificationRepository extends JpaRepository<TypeIdentificationEntity, Integer> {

    List<TypeIdentificationEntity> findByIdState(Integer idState);

    Optional<TypeIdentificationEntity> findByTypeIdentification(String typeIdentification);
}
