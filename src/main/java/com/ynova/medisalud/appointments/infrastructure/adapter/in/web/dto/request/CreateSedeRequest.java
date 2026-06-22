package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSedeRequest(
        @NotBlank @Size(min = 3, max = 100) String name,
        @NotBlank String address,
        @NotBlank String city,
        String phone
) {}
