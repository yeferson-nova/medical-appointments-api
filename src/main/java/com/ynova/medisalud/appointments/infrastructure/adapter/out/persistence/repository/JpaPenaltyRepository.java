package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.PenaltyEntity;

public interface JpaPenaltyRepository extends JpaRepository<PenaltyEntity, Long> {

    long countByPatientIdAndPenaltyDateAfter(Long patientId, LocalDateTime since);
}
