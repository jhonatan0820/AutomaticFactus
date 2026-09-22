package com.automaticfactus.dtos;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;



public record RequestOrders(

	    @NotNull
	    Integer idClient,

	    @NotNull
	    @PositiveOrZero
	    Long totalOrderPrice,

	    @NotNull
	    LocalDate orderDate,

	    @NotNull
	    LocalDate effectiveDate,

	    @NotNull
	    Integer idTypeOrder,

	    @NotNull
		Integer orderNumber,

		List<OrderItemRequest> items

		) {

		public record OrderItemRequest(
				String descripcion,
				String talla,
				Integer cantidad,
				Long precioUnitario,
				Boolean domicilio,
				Integer precioDomicilio
		) {}

	}
