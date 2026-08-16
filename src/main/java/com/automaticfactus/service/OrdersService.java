package com.automaticfactus.service;

import com.automaticfactus.entities.OrderEntity;
import com.automaticfactus.model.SaveResult;
import com.automaticfactus.repositories.OrderRepository;

import Dtos.RequestOrders;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

	private final OrderRepository repo;
	private final DatabaseService db;

	public OrdersService(OrderRepository repo, DatabaseService db) {
		this.repo = repo;
		this.db = db;
	}

	public SaveResult saveOrder(RequestOrders req) {
		if (!db.isReady()) return SaveResult.UNAVAILABLE;

		OrderEntity o = new OrderEntity();
		o.setIdClient(req.idClient());
		o.setTotalOrderPrice(req.totalOrderPrice());
		o.setOrderDate(req.orderDate());
		o.setEffectiveDate(req.effectiveDate());
		o.setIdTypeOrder(req.idTypeOrder());
		o.setOrderNumber(req.orderNumber());

		OrderEntity saved = repo.save(o);
		return saved.getIdOrder() != null ? SaveResult.CREATED : SaveResult.UNAVAILABLE;
	}

	/**
	 * Próximo OrderNumber para un tipo de orden. Se basa en la orden más
	 * reciente (mayor IdOrder) con ese IdTypeOrder. Devuelve null si la BD
	 * no está lista.
	 */
	public Integer nextOrderNumber(int idTypeOrder) {
		if (!db.isReady()) return null;
		int last = repo.findTopByIdTypeOrderOrderByIdOrderDesc(idTypeOrder)
				.map(o -> o.getOrderNumber() == null ? 0 : o.getOrderNumber())
				.orElse(0);
		return last + 1;
	}
}
