package com.automaticfactus.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdOrder")
    private Integer idOrder;

    @Column(name = "IdClient")
    private Integer idClient;

    @Column(name = "TotalOrderPrice")
    private Long totalOrderPrice;

    @Column(name = "OrderDate")
    private LocalDate orderDate;

    @Column(name = "EffectiveDate")
    private LocalDate effectiveDate;

    @Column(name = "IdTypeOrder")
    private Integer idTypeOrder;

    @Column(name = "OrderNumber")
    private Integer orderNumber;

    public OrderEntity() {}

    public Integer getIdOrder() { return idOrder; }
    public void setIdOrder(Integer idOrder) { this.idOrder = idOrder; }
    public Integer getIdClient() { return idClient; }
    public void setIdClient(Integer idClient) { this.idClient = idClient; }
    public Long getTotalOrderPrice() { return totalOrderPrice; }
    public void setTotalOrderPrice(Long totalOrderPrice) { this.totalOrderPrice = totalOrderPrice; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public Integer getIdTypeOrder() { return idTypeOrder; }
    public void setIdTypeOrder(Integer idTypeOrder) { this.idTypeOrder = idTypeOrder; }
    public Integer getOrderNumber() { return orderNumber; }
    public void setOrderNumber(Integer orderNumber) { this.orderNumber = orderNumber; }
}
