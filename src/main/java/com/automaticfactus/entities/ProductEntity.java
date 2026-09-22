package com.automaticfactus.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @Column(name = "IdProduct")
    private Integer idProduct;

    @Column(name = "ProductNumber")
    private Integer productNumber;

    @Column(name = "Description")
    private String description;

    @Column(name = "Size")
    private Integer size;

    @Column(name = "Price")
    private Long price;

    public Integer getIdProduct() { return idProduct; }
    public void setIdProduct(Integer idProduct) { this.idProduct = idProduct; }
    public Integer getProductNumber() { return productNumber; }
    public void setProductNumber(Integer productNumber) { this.productNumber = productNumber; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
}