package com.automaticfactus.repositories;

import com.automaticfactus.entities.ProductEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    Optional<ProductEntity> findFirstByDescriptionAndSize(String description, Integer size);

    @Query("SELECT COALESCE(MAX(p.idProduct), 0) FROM ProductEntity p")
    int findMaxIdProduct();
}