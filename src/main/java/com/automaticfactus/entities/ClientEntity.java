package com.automaticfactus.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdClient")
    private Integer idClient;

    @Column(name = "Name", length = 200, nullable = false)
    private String name;

    @Column(name = "IdTypeIdentification", nullable = false)
    private Integer idTypeIdentification;

    @Column(name = "IdentificationNumber")
    private Long identificationNumber;

    @Column(name = "Cellphone")
    private Long cellphone;

    @Column(name = "Address", length = 200)
    private String address;

    @Column(name = "Email", length = 200)
    private String email;

    public ClientEntity() {}

    public Integer getIdClient() { return idClient; }
    public void setIdClient(Integer idClient) { this.idClient = idClient; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getIdTypeIdentification() { return idTypeIdentification; }
    public void setIdTypeIdentification(Integer idTypeIdentification) { this.idTypeIdentification = idTypeIdentification; }
    public Long getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(Long identificationNumber) { this.identificationNumber = identificationNumber; }
    public Long getCellphone() { return cellphone; }
    public void setCellphone(Long cellphone) { this.cellphone = cellphone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
