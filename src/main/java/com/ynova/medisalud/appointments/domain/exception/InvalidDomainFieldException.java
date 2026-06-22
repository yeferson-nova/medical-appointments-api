package com.ynova.medisalud.appointments.domain.exception;

public class InvalidDomainFieldException extends DomainException {

    private final String field;

    public InvalidDomainFieldException(String field, String message) {
        super("INVALID_FIELD", message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
