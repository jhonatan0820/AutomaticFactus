package com.automaticfactus.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "listtypeidentification")
public class TypeIdentificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTypeIdentification")
    private Integer idTypeIdentification;

    @Column(name = "TypeIdentification")
    private String typeIdentification;

    @Column(name = "Description")
    private String description;

    @Column(name = "IdState")
    private Integer idState;

    public TypeIdentificationEntity() {}

    public Integer getIdTypeIdentification() { return idTypeIdentification; }
    public void setIdTypeIdentification(Integer idTypeIdentification) { this.idTypeIdentification = idTypeIdentification; }
    public String getTypeIdentification() { return typeIdentification; }
    public void setTypeIdentification(String typeIdentification) { this.typeIdentification = typeIdentification; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getIdState() { return idState; }
    public void setIdState(Integer idState) { this.idState = idState; }
}
