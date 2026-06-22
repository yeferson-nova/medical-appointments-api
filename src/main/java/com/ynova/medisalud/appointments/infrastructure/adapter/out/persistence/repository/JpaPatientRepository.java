package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.PatientEntity;

public interface JpaPatientRepository extends JpaRepository<PatientEntity, Long> {

    boolean existsByDocumentId(String documentId);
}
