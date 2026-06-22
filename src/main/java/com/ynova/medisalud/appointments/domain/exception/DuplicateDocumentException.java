package com.ynova.medisalud.appointments.domain.exception;

public class DuplicateDocumentException extends DomainException {

    public DuplicateDocumentException(String documentId) {
        super("DUPLICATE_DOCUMENT", "Ya existe un paciente con documento " + documentId);
    }
}
