package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record RescheduleAppointmentRequest(
        @NotNull @Future LocalDateTime newDateTime
) {}
