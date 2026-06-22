package com.ynova.medisalud.appointments.domain.event;

public record AppointmentCancelledEvent(
        Long appointmentId,
        Long patientId,
        boolean penaltyApplied
) {}
