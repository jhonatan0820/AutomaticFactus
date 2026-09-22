package com.automaticfactus.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "orderdetails")
@IdClass(OrderDetailId.class)
public class OrderDetailEntity {

    @Id
    @Column(name = "IdOrder")
    private Integer idOrder;

    @Id
    @Column(name = "IdProduct")
    private Integer idProduct;

    @Column(name = "Amount")
    private Integer amount;

    @Column(name = "IndividualPrice")
    private Long individualPrice;

    @Column(name = "HomeDeliveryService")
    private Boolean homeDeliveryService;

    @Column(name = "PriceHomeDeliveryService")
    private Integer priceHomeDeliveryService;

    @Column(name = "TotalPrice")
    private Long totalPrice;

    public Integer getIdOrder() { return idOrder; }
    public void setIdOrder(Integer idOrder) { this.idOrder = idOrder; }
    public Integer getIdProduct() { return idProduct; }
    public void setIdProduct(Integer idProduct) { this.idProduct = idProduct; }
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public Long getIndividualPrice() { return individualPrice; }
    public void setIndividualPrice(Long individualPrice) { this.individualPrice = individualPrice; }
    public Boolean getHomeDeliveryService() { return homeDeliveryService; }
    public void setHomeDeliveryService(Boolean homeDeliveryService) { this.homeDeliveryService = homeDeliveryService; }
    public Integer getPriceHomeDeliveryService() { return priceHomeDeliveryService; }
    public void setPriceHomeDeliveryService(Integer priceHomeDeliveryService) { this.priceHomeDeliveryService = priceHomeDeliveryService; }
    public Long getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Long totalPrice) { this.totalPrice = totalPrice; }
}