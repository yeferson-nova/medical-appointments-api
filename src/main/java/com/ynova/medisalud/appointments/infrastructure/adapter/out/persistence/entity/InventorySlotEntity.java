package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(name = "inventory_slots",
       uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "sede_id", "slot_date", "time_start"}))
public class InventorySlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "sede_id", nullable = false)
    private Long sedeId;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "time_start", nullable = false)
    private LocalTime timeStart;

    @Column(name = "time_end", nullable = false)
    private LocalTime timeEnd;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @Version
    private Long version;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public Long getSedeId() { return sedeId; }
    public void setSedeId(Long sedeId) { this.sedeId = sedeId; }
    public LocalDate getSlotDate() { return slotDate; }
    public void setSlotDate(LocalDate slotDate) { this.slotDate = slotDate; }
    public LocalTime getTimeStart() { return timeStart; }
    public void setTimeStart(LocalTime timeStart) { this.timeStart = timeStart; }
    public LocalTime getTimeEnd() { return timeEnd; }
    public void setTimeEnd(LocalTime timeEnd) { this.timeEnd = timeEnd; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
