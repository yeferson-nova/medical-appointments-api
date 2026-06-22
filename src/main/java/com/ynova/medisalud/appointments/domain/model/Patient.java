package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDate;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

public class Patient {

    private final Long id;
    private final String name;
    private final String documentId;
    private final String phone;
    private final String email;
    private final LocalDate birthDate;

    public Patient(Long id, String name, String documentId, String phone, String email, LocalDate birthDate) {
        if (name == null || name.isBlank()) {
            throw new InvalidDomainFieldException("name", "El nombre del paciente es obligatorio");
        }
        if (name.length() < 3 || name.length() > 100) {
            throw new InvalidDomainFieldException("name", "El nombre debe tener entre 3 y 100 caracteres");
        }
        if (documentId == null || documentId.isBlank()) {
            throw new InvalidDomainFieldException("documentId", "El documento de identidad es obligatorio");
        }
        if (documentId.length() < 7) {
            throw new InvalidDomainFieldException("documentId", "El documento debe tener al menos 7 caracteres");
        }
        if (phone == null || phone.isBlank()) {
            throw new InvalidDomainFieldException("phone", "El telefono es obligatorio");
        }
        if (!phone.matches("\\d{7,}")) {
            throw new InvalidDomainFieldException("phone", "El telefono debe tener al menos 7 digitos");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidDomainFieldException("email", "El email es obligatorio");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidDomainFieldException("email", "El formato del email no es valido");
        }
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new InvalidDomainFieldException("birthDate", "La fecha de nacimiento no puede ser futura");
        }
        this.id = id;
        this.name = name.trim();
        this.documentId = documentId.trim();
        this.phone = phone;
        this.email = email.trim();
        this.birthDate = birthDate;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDocumentId() { return documentId; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public LocalDate getBirthDate() { return birthDate; }
}
