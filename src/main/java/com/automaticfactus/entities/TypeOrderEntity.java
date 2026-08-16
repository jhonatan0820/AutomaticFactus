package com.automaticfactus.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "listtypeorder")
public class TypeOrderEntity {

    @Id
    @Column(name = "IdTypeOrder")
    private Integer idTypeOrder;

    @Column(name = "TypeOrder")
    private String typeOrder;

    @Column(name = "IdState")
    private Integer idState;

    public TypeOrderEntity() {}

    public Integer getIdTypeOrder() { return idTypeOrder; }
    public void setIdTypeOrder(Integer idTypeOrder) { this.idTypeOrder = idTypeOrder; }
    public String getTypeOrder() { return typeOrder; }
    public void setTypeOrder(String typeOrder) { this.typeOrder = typeOrder; }
    public Integer getIdState() { return idState; }
    public void setIdState(Integer idState) { this.idState = idState; }
}
