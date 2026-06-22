package com.ynova.medisalud.appointments.domain.event;

public record PenaltyRegisteredEvent(
        Long patientId,
        Long penaltyId,
        long totalPenaltiesLast30Days
) {}
