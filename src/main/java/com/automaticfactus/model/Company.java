package com.automaticfactus.model;

public class Company {
    private final String name;
    private final String nit;
    private final String phone;
    private final String city;

    public Company(String name, String nit, String phone, String city) {
        this.name = name;
        this.nit = nit;
        this.phone = phone;
        this.city = city;
    }

    public String getName() { return name; }
    public String getNit() { return nit; }
    public String getPhone() { return phone; }
    public String getCity() { return city; }
}
