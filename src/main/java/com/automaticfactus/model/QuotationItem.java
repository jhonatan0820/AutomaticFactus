package com.automaticfactus.model;

import java.math.BigDecimal;

public class QuotationItem {
    private final String description;
    private final String size;
    private final int quantity;
    private final BigDecimal unitPrice;

    public QuotationItem(String description, String size, int quantity, BigDecimal unitPrice) {
        this.description = description;
        this.size = size;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getDescription() { return description; }
    public String getSize() { return size; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }

    public BigDecimal getTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

