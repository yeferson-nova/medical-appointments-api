package com.ynova.medisalud.appointments.domain.model;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

public class Doctor {

    private final Long id;
    private final String name;
    private final String specialty;
    private final String phone;
    private final String email;

    public Doctor(Long id, String name, String specialty, String phone, String email) {
        if (name == null || name.isBlank()) {
            throw new InvalidDomainFieldException("name", "El nombre del medico es obligatorio");
        }
        if (name.length() < 3 || name.length() > 100) {
            throw new InvalidDomainFieldException("name", "El nombre debe tener entre 3 y 100 caracteres");
        }
        if (specialty == null || specialty.isBlank()) {
            throw new InvalidDomainFieldException("specialty", "La especialidad es obligatoria");
        }
        if (phone != null && !phone.isBlank() && !phone.matches("\\d{7,}")) {
            throw new InvalidDomainFieldException("phone", "El telefono debe tener al menos 7 digitos");
        }
        if (email != null && !email.isBlank() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidDomainFieldException("email", "El formato del email no es valido");
        }
        this.id = id;
        this.name = name.trim();
        this.specialty = specialty.trim();
        this.phone = phone;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
