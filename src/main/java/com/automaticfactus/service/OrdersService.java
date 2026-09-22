package com.automaticfactus.service;

import com.automaticfactus.entities.OrderEntity;
import com.automaticfactus.model.SaveResult;
import com.automaticfactus.repositories.OrderRepository;
import com.automaticfactus.repositories.OrderDetailRepository;
import com.automaticfactus.repositories.ProductRepository;
import com.automaticfactus.entities.OrderDetailEntity;
import com.automaticfactus.entities.ProductEntity;

import com.automaticfactus.dtos.RequestOrders;
import com.automaticfactus.dtos.RequestOrders.OrderItemRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

	private final OrderRepository repo;
	 private final OrderDetailRepository detailRepo;
	 private final ProductRepository productRepo;
	private final DatabaseService db;

	 public OrdersService(OrderRepository repo, OrderDetailRepository detailRepo,
	                      ProductRepository productRepo, DatabaseService db) {
		this.repo = repo;
			this.detailRepo = detailRepo;
			this.productRepo = productRepo;
		this.db = db;
	}

		@Transactional
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
		if (saved.getIdOrder() == null) return SaveResult.UNAVAILABLE;
		for (OrderItemRequest item : req.items() == null ? java.util.List.<OrderItemRequest>of() : req.items()) {
			if (item == null || item.descripcion() == null || item.descripcion().isBlank()) continue;
			int amount = item.cantidad() == null || item.cantidad() < 1 ? 1 : item.cantidad();
			long individualPrice = item.precioUnitario() == null ? 0L : item.precioUnitario();
			Integer size = parseSize(item.talla());
			ProductEntity product = productRepo.findFirstByDescriptionAndSize(item.descripcion().trim(), size)
					.orElseGet(() -> newProduct(item, size));
			product = productRepo.save(product);

			OrderDetailEntity detail = new OrderDetailEntity();
			detail.setIdOrder(saved.getIdOrder());
			detail.setIdProduct(product.getIdProduct());
			detail.setAmount(amount);
			detail.setIndividualPrice(individualPrice);
			detail.setHomeDeliveryService(Boolean.TRUE.equals(item.domicilio()));
			detail.setPriceHomeDeliveryService(item.precioDomicilio());
			detail.setTotalPrice(individualPrice * amount);
			detailRepo.save(detail);
		}
		return saved.getIdOrder() != null ? SaveResult.CREATED : SaveResult.UNAVAILABLE;
	}

	private ProductEntity newProduct(OrderItemRequest item, Integer size) {
		ProductEntity product = new ProductEntity();
		int nextId = productRepo.findMaxIdProduct() + 1;
		product.setIdProduct(nextId);
		product.setProductNumber(nextId);
		product.setDescription(item.descripcion().trim());
		product.setSize(size);
		product.setPrice(item.precioUnitario());
		return product;
	}

	private Integer parseSize(String raw) {
		if (raw == null || raw.isBlank()) return null;
		try { return Integer.valueOf(raw.trim()); }
		catch (NumberFormatException ignored) { return null; }
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
