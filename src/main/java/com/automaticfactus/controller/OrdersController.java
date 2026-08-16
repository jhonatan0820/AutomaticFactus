package com.automaticfactus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.automaticfactus.model.SaveResult;
import com.automaticfactus.service.OrdersService;

import Dtos.RequestOrders;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

	private final OrdersService orders;

	public OrdersController(OrdersService orders) {
		this.orders = orders;
	}

	@PostMapping("/guardarOrden")
	public ResponseEntity<Void> saveOrder(@RequestBody RequestOrders requestOrder) {
		if (requestOrder == null
				|| requestOrder.idClient() == null
				|| requestOrder.idTypeOrder() == null
				|| requestOrder.orderDate() == null
				|| requestOrder.effectiveDate() == null
				|| requestOrder.totalOrderPrice() == null
				|| requestOrder.orderNumber() == null) {
			return ResponseEntity.badRequest().build();
		}
		try {
			SaveResult result = orders.saveOrder(requestOrder);
			return switch (result) {
				case CREATED     -> ResponseEntity.status(201).build();
				case UPDATED     -> ResponseEntity.ok().build();
				case UNAVAILABLE -> ResponseEntity.status(503).build();
			};
		} catch (Exception e) {
			System.err.println("[/api/orders/guardarOrden] "
					+ e.getClass().getSimpleName() + ": " + e.getMessage());
			return ResponseEntity.status(500).build();
		}
	}

	/**
	 * Próximo OrderNumber para el tipo indicado (cotización/factura tienen
	 * su propia serie). Se usa desde el front al elegir tipo y tras cada
	 * descarga para reflejar el número que se persistirá.
	 */
	@GetMapping("/proximo-numero")
	public ResponseEntity<?> nextOrderNumber(@RequestParam("idTypeOrder") int idTypeOrder) {
		try {
			Integer next = orders.nextOrderNumber(idTypeOrder);
			if (next == null) return ResponseEntity.status(503).build();
			return ResponseEntity.ok(java.util.Map.of("numero", next));
		} catch (Exception e) {
			System.err.println("[/api/orders/proximo-numero] "
					+ e.getClass().getSimpleName() + ": " + e.getMessage());
			return ResponseEntity.status(500).build();
		}
	}
	
	
	@PostMapping("/guardardetallesOrden")
	public ResponseEntity<Void> SaveOrderdetails(){
		return null;
	}
	
}
