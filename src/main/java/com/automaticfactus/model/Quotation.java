package com.automaticfactus.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Quotation {
    private final Company company;
    private final Customer customer;
    private final String number;
    private final LocalDate date;
    private final LocalDate validUntil;
    private final List<QuotationItem> items;
    private final String notes;

    public Quotation(Company company, Customer customer, String number,
                     LocalDate date, LocalDate validUntil,
                     List<QuotationItem> items, String notes) {
        this.company = company;
        this.customer = customer;
        this.number = number;
        this.date = date;
        this.validUntil = validUntil;
        this.items = items;
        this.notes = notes;
    }

    public Company getCompany() { return company; }
    public Customer getCustomer() { return customer; }
    public String getNumber() { return number; }
    public LocalDate getDate() { return date; }
    public LocalDate getValidUntil() { return validUntil; }
    public List<QuotationItem> getItems() { return items; }
    public String getNotes() { return notes; }

    public BigDecimal getSubtotal() {
        return items.stream()
                .map(QuotationItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotal() {
        return getSubtotal();
    }
}
