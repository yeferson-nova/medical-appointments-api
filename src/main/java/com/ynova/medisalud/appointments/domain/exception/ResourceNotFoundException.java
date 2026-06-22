package com.ynova.medisalud.appointments.domain.exception;

public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super("RESOURCE_NOT_FOUND", resourceName + " con id " + id + " no fue encontrado");
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super("RESOURCE_NOT_FOUND", resourceName + " con identificador " + identifier + " no fue encontrado");
    }
}
