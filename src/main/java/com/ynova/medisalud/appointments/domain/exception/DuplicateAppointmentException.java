package com.ynova.medisalud.appointments.domain.exception;

import java.time.LocalDateTime;

public class DuplicateAppointmentException extends DomainException {

    public DuplicateAppointmentException(Long patientId, Long doctorId, LocalDateTime dateTime) {
        super("DUPLICATE_APPOINTMENT",
                "El paciente " + patientId + " ya tiene una cita con el medico " + doctorId
                        + " en la franja " + dateTime);
    }
}
