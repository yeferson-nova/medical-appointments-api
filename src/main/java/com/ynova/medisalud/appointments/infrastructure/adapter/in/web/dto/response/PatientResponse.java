package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response;

import java.time.LocalDate;

public record PatientResponse(Long id, String name, String documentId, String phone,
                               String email, LocalDate birthDate) {}
