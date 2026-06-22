package com.ynova.medisalud.appointments.domain.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.ynova.medisalud.appointments.domain.exception.InvalidAppointmentStateException;
import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

public class Appointment {

    private static final long PENALTY_THRESHOLD_HOURS = 2;

    private final Long id;
    private final Long doctorId;
    private final Long patientId;
    private final Long sedeId;
    private final Long slotId;
    private final LocalDateTime dateTime;
    private AppointmentStatus status;
    private LocalDateTime cancellationTimestamp;
    private boolean penaltyApplied;
    private long version;

    public Appointment(Long id, Long doctorId, Long patientId, Long sedeId, Long slotId,
                       LocalDateTime dateTime, AppointmentStatus status,
                       LocalDateTime cancellationTimestamp, boolean penaltyApplied, long version) {
        if (doctorId == null) {
            throw new InvalidDomainFieldException("doctorId", "El id del medico es obligatorio");
        }
        if (patientId == null) {
            throw new InvalidDomainFieldException("patientId", "El id del paciente es obligatorio");
        }
        if (sedeId == null) {
            throw new InvalidDomainFieldException("sedeId", "El id de la sede es obligatorio");
        }
        if (dateTime == null) {
            throw new InvalidDomainFieldException("dateTime", "La fecha y hora son obligatorias");
        }
        if (status == null) {
            throw new InvalidDomainFieldException("status", "El estado es obligatorio");
        }
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.sedeId = sedeId;
        this.slotId = slotId;
        this.dateTime = dateTime;
        this.status = status;
        this.cancellationTimestamp = cancellationTimestamp;
        this.penaltyApplied = penaltyApplied;
        this.version = version;
    }

    public static Appointment create(Long doctorId, Long patientId, Long sedeId,
                                     Long slotId, LocalDateTime dateTime) {
        return new Appointment(null, doctorId, patientId, sedeId, slotId,
                dateTime, AppointmentStatus.PROGRAMADA, null, false, 0);
    }

    public boolean cancel(LocalDateTime now) {
        if (status != AppointmentStatus.PROGRAMADA) {
            throw new InvalidAppointmentStateException(
                    "Solo se pueden cancelar citas con estado PROGRAMADA, estado actual: " + status);
        }
        this.status = AppointmentStatus.CANCELADA;
        this.cancellationTimestamp = now;

        long hoursUntilAppointment = ChronoUnit.HOURS.between(now, dateTime);
        if (hoursUntilAppointment < PENALTY_THRESHOLD_HOURS) {
            this.penaltyApplied = true;
            return true;
        }
        return false;
    }

    public Long getId() { return id; }
    public Long getDoctorId() { return doctorId; }
    public Long getPatientId() { return patientId; }
    public Long getSedeId() { return sedeId; }
    public Long getSlotId() { return slotId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public AppointmentStatus getStatus() { return status; }
    public LocalDateTime getCancellationTimestamp() { return cancellationTimestamp; }
    public boolean isPenaltyApplied() { return penaltyApplied; }
    public long getVersion() { return version; }
}
