package com.automaticfactus.repositories;

import com.automaticfactus.entities.OrderEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query("SELECT COALESCE(MAX(o.idOrder), 0) FROM OrderEntity o")
    int findMaxIdOrder();

    /**
     * Trae la orden más reciente (mayor IdOrder) para un IdTypeOrder dado.
     * Se resuelve como un solo index seek sobre (IdTypeOrder, IdOrder DESC),
     * sin subconsultas. La usa {@code OrdersService.nextOrderNumber} para
     * calcular el siguiente OrderNumber de esa serie.
     */
    Optional<OrderEntity> findTopByIdTypeOrderOrderByIdOrderDesc(int idTypeOrder);
}
