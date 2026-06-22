package com.ynova.medisalud.appointments.domain.exception;

public class PatientBlockedException extends DomainException {

    public PatientBlockedException(Long patientId, long penaltyCount) {
        super("PATIENT_BLOCKED",
                "El paciente " + patientId + " tiene " + penaltyCount
                        + " penalizaciones en los ultimos 30 dias y no puede agendar citas");
    }
}
