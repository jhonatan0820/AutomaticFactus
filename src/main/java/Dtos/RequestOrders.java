package Dtos;

import java.time.LocalDate;
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
	    Integer orderNumber

	) {}
