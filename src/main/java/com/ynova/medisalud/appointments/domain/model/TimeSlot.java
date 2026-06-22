package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDateTime;

public record TimeSlot(LocalDateTime start, LocalDateTime end) {

    public static TimeSlot of(LocalDateTime start) {
        if (start == null) {
            throw new IllegalArgumentException("El inicio del slot no puede ser nulo");
        }
        return new TimeSlot(start, start.plusMinutes(30));
    }
}
