package com.ynova.medisalud.appointments.domain.event;

public record AppointmentRescheduledEvent(
        Long oldAppointmentId,
        Long newAppointmentId
) {}
