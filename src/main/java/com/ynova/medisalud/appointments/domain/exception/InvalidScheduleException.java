package com.ynova.medisalud.appointments.domain.exception;

public class InvalidScheduleException extends DomainException {

    public InvalidScheduleException(String message) {
        super("INVALID_SCHEDULE", message);
    }
}
