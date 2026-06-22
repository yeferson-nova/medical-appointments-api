package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long doctorId,
        Long patientId,
        Long sedeId,
        LocalDateTime dateTime,
        String status,
        LocalDateTime cancellationTimestamp,
        boolean penaltyApplied
) {}
