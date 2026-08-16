package com.automaticfactus.repositories;

import com.automaticfactus.entities.TypeOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TypeOrderRepository extends JpaRepository<TypeOrderEntity, Integer> {

    List<TypeOrderEntity> findByIdState(Integer idState);
}
