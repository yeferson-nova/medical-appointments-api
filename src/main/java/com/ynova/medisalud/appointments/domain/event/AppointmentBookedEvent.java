package com.ynova.medisalud.appointments.domain.event;

import java.time.LocalDateTime;

public record AppointmentBookedEvent(
        Long appointmentId,
        Long doctorId,
        Long patientId,
        Long sedeId,
        LocalDateTime dateTime
) {}
