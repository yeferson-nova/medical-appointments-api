package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record BookAppointmentRequest(
        @NotNull Long patientId,
        @NotNull Long doctorId,
        @NotNull Long sedeId,
        @NotNull @Future LocalDateTime dateTime
) {}
