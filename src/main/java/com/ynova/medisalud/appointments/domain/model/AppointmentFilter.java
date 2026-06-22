package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDate;

public record AppointmentFilter(
        Long sedeId,
        Long doctorId,
        Long patientId,
        AppointmentStatus status,
        LocalDate startDate,
        LocalDate endDate
) {}
