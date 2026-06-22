package com.ynova.medisalud.appointments.domain.port.out;

import java.time.LocalDateTime;

import com.ynova.medisalud.appointments.domain.model.Penalty;

public interface PenaltyRepositoryPort {

    Penalty save(Penalty penalty);

    long countByPatientIdAndDateAfter(Long patientId, LocalDateTime since);
}
