package com.ynova.medisalud.appointments.infrastructure.adapter.in.web;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ynova.medisalud.appointments.domain.exception.DuplicateAppointmentException;
import com.ynova.medisalud.appointments.domain.exception.DuplicateDocumentException;
import com.ynova.medisalud.appointments.domain.exception.InvalidAppointmentStateException;
import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;
import com.ynova.medisalud.appointments.domain.exception.InvalidScheduleException;
import com.ynova.medisalud.appointments.domain.exception.PatientBlockedException;
import com.ynova.medisalud.appointments.domain.exception.ResourceNotFoundException;
import com.ynova.medisalud.appointments.domain.exception.SlotNotAvailableException;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getErrorCode(), ex.getMessage(), request, null);
    }

    @ExceptionHandler(SlotNotAvailableException.class)
    public ResponseEntity<ApiErrorResponse> handleSlotNotAvailable(SlotNotAvailableException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getErrorCode(), ex.getMessage(), request, null);
    }

    @ExceptionHandler(DuplicateAppointmentException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateAppointment(DuplicateAppointmentException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getErrorCode(), ex.getMessage(), request, null);
    }

    @ExceptionHandler(DuplicateDocumentException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateDocument(DuplicateDocumentException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getErrorCode(), ex.getMessage(), request, null);
    }

    @ExceptionHandler(PatientBlockedException.class)
    public ResponseEntity<ApiErrorResponse> handlePatientBlocked(PatientBlockedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getErrorCode(), ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidScheduleException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSchedule(InvalidScheduleException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidDomainFieldException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidField(InvalidDomainFieldException ex, HttpServletRequest request) {
        var fieldErrors = List.of(new ApiErrorResponse.FieldError(ex.getField(), ex.getMessage()));
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), request, fieldErrors);
    }

    @ExceptionHandler(InvalidAppointmentStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidState(InvalidAppointmentStateException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ApiErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                "Error de validacion en los campos enviados", request, fieldErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "Error interno del servidor", request, null);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String errorCode,
                                                            String message, HttpServletRequest request,
                                                            List<ApiErrorResponse.FieldError> fieldErrors) {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                errorCode,
                message,
                MDC.get("requestId"),
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(status).body(response);
    }
}
