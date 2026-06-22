package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePatientRequest(
        @NotBlank @Size(min = 3, max = 100) String name,
        @NotBlank @Size(min = 7) String documentId,
        @NotBlank @Pattern(regexp = "\\d{7,}", message = "El telefono debe tener al menos 7 digitos") String phone,
        @NotBlank @Email String email,
        @Past LocalDate birthDate
) {}
