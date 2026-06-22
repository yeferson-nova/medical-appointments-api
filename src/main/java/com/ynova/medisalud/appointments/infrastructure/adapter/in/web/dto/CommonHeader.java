package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto;

import java.time.Instant;

public record CommonHeader(
        String requestId,
        String clientIp,
        String userAgent,
        String idempotencyKey,
        Instant requestTime
) {}
