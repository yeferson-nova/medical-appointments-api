package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String errorCode,
        String message,
        String requestId,
        String path,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {}
}
