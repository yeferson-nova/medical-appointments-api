package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity;

import java.time.LocalDateTime;

import com.ynova.medisalud.appointments.domain.model.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "appointments")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "sede_id", nullable = false)
    private Long sedeId;

    @Column(name = "slot_id")
    private Long slotId;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @Column(name = "cancellation_timestamp")
    private LocalDateTime cancellationTimestamp;

    @Column(name = "penalty_applied", nullable = false)
    private boolean penaltyApplied;

    @Version
    private Long version;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getSedeId() { return sedeId; }
    public void setSedeId(Long sedeId) { this.sedeId = sedeId; }
    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public LocalDateTime getCancellationTimestamp() { return cancellationTimestamp; }
    public void setCancellationTimestamp(LocalDateTime cancellationTimestamp) { this.cancellationTimestamp = cancellationTimestamp; }
    public boolean isPenaltyApplied() { return penaltyApplied; }
    public void setPenaltyApplied(boolean penaltyApplied) { this.penaltyApplied = penaltyApplied; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
