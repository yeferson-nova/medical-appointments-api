package com.ynova.medisalud.appointments.domain.exception;

public class InvalidAppointmentStateException extends DomainException {

    public InvalidAppointmentStateException(String message) {
        super("INVALID_APPOINTMENT_STATE", message);
    }
}
