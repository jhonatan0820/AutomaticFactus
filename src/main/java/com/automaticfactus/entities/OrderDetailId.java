package com.automaticfactus.entities;

import java.io.Serializable;
import java.util.Objects;

public class OrderDetailId implements Serializable {
    private Integer idOrder;
    private Integer idProduct;

    public OrderDetailId() {}

    public OrderDetailId(Integer idOrder, Integer idProduct) {
        this.idOrder = idOrder;
        this.idProduct = idProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailId other)) return false;
        return Objects.equals(idOrder, other.idOrder) && Objects.equals(idProduct, other.idProduct);
    }

    @Override
    public int hashCode() { return Objects.hash(idOrder, idProduct); }
}