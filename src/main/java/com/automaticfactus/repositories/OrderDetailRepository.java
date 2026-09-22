package com.automaticfactus.repositories;

import com.automaticfactus.entities.OrderDetailEntity;
import com.automaticfactus.entities.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, OrderDetailId> {}