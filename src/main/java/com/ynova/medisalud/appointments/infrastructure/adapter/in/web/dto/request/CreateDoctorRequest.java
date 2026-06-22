package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateDoctorRequest(
        @NotBlank @Size(min = 3, max = 100) String name,
        @NotBlank String specialty,
        @Pattern(regexp = "\\d{7,}", message = "El telefono debe tener al menos 7 digitos") String phone,
        @Email String email
) {}
