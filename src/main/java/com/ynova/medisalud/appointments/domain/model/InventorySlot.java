package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

public class InventorySlot {

    private final Long id;
    private final Long doctorId;
    private final Long sedeId;
    private final LocalDate slotDate;
    private final LocalTime timeStart;
    private final LocalTime timeEnd;
    private boolean available;
    private long version;

    public InventorySlot(Long id, Long doctorId, Long sedeId, LocalDate slotDate,
                         LocalTime timeStart, LocalTime timeEnd, boolean available, long version) {
        if (doctorId == null) {
            throw new InvalidDomainFieldException("doctorId", "El id del medico es obligatorio");
        }
        if (sedeId == null) {
            throw new InvalidDomainFieldException("sedeId", "El id de la sede es obligatorio");
        }
        if (slotDate == null) {
            throw new InvalidDomainFieldException("slotDate", "La fecha del slot es obligatoria");
        }
        if (timeStart == null) {
            throw new InvalidDomainFieldException("timeStart", "La hora de inicio es obligatoria");
        }
        if (timeEnd == null) {
            throw new InvalidDomainFieldException("timeEnd", "La hora de fin es obligatoria");
        }
        this.id = id;
        this.doctorId = doctorId;
        this.sedeId = sedeId;
        this.slotDate = slotDate;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.available = available;
        this.version = version;
    }

    public void reserve() {
        if (!available) {
            throw new SlotAlreadyReservedException();
        }
        this.available = false;
    }

    public void release() {
        this.available = true;
    }

    public Long getId() { return id; }
    public Long getDoctorId() { return doctorId; }
    public Long getSedeId() { return sedeId; }
    public LocalDate getSlotDate() { return slotDate; }
    public LocalTime getTimeStart() { return timeStart; }
    public LocalTime getTimeEnd() { return timeEnd; }
    public boolean isAvailable() { return available; }
    public long getVersion() { return version; }

    private static class SlotAlreadyReservedException extends RuntimeException {
        SlotAlreadyReservedException() {
            super("El slot ya esta reservado");
        }
    }
}
