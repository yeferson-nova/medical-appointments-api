package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response;

import java.time.LocalDateTime;

public record TimeSlotResponse(LocalDateTime start, LocalDateTime end) {}
