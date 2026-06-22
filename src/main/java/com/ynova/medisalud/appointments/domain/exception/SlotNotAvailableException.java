package com.ynova.medisalud.appointments.domain.exception;

import java.time.LocalDateTime;

public class SlotNotAvailableException extends DomainException {

    public SlotNotAvailableException(Long doctorId, LocalDateTime dateTime) {
        super("SLOT_NOT_AVAILABLE",
                "El medico con id " + doctorId + " ya tiene una cita en la franja " + dateTime);
    }

    public SlotNotAvailableException(String message) {
        super("SLOT_NOT_AVAILABLE", message);
    }
}
