package com.ynova.medisalud.appointments.domain.model;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

public class Sede {

    private final Long id;
    private final String name;
    private final String address;
    private final String city;
    private final String phone;

    public Sede(Long id, String name, String address, String city, String phone) {
        if (name == null || name.isBlank()) {
            throw new InvalidDomainFieldException("name", "El nombre de la sede es obligatorio");
        }
        if (name.length() < 3 || name.length() > 100) {
            throw new InvalidDomainFieldException("name", "El nombre debe tener entre 3 y 100 caracteres");
        }
        if (address == null || address.isBlank()) {
            throw new InvalidDomainFieldException("address", "La direccion es obligatoria");
        }
        if (city == null || city.isBlank()) {
            throw new InvalidDomainFieldException("city", "La ciudad es obligatoria");
        }
        this.id = id;
        this.name = name.trim();
        this.address = address.trim();
        this.city = city.trim();
        this.phone = phone;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getPhone() { return phone; }
}
