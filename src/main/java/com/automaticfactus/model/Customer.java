package com.automaticfactus.model;

public class Customer {
    private final String name;
    private final String idType;
    private final String nit;
    private final String phone;
    private final String email;
    private final String address;

    public Customer(String name, String idType, String nit, String phone, String email, String address) {
        this.name = name;
        this.idType = idType;
        this.nit = nit;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getName() { return name; }
    public String getIdType() { return idType; }
    public String getNit() { return nit; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
}


