package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDateTime;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

public class Penalty {

    private final Long id;
    private final Long patientId;
    private final Long appointmentId;
    private final LocalDateTime penaltyDate;

    public Penalty(Long id, Long patientId, Long appointmentId, LocalDateTime penaltyDate) {
        if (patientId == null) {
            throw new InvalidDomainFieldException("patientId", "El id del paciente es obligatorio");
        }
        if (appointmentId == null) {
            throw new InvalidDomainFieldException("appointmentId", "El id de la cita es obligatorio");
        }
        if (penaltyDate == null) {
            throw new InvalidDomainFieldException("penaltyDate", "La fecha de penalizacion es obligatoria");
        }
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.penaltyDate = penaltyDate;
    }

    public static Penalty create(Long patientId, Long appointmentId, LocalDateTime penaltyDate) {
        return new Penalty(null, patientId, appointmentId, penaltyDate);
    }

    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public Long getAppointmentId() { return appointmentId; }
    public LocalDateTime getPenaltyDate() { return penaltyDate; }
}
