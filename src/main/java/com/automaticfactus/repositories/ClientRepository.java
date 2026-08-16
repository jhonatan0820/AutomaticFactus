package com.automaticfactus.repositories;

import com.automaticfactus.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    Optional<ClientEntity> findByIdTypeIdentificationAndIdentificationNumber(
            Integer idTypeIdentification, Long identificationNumber);
}
