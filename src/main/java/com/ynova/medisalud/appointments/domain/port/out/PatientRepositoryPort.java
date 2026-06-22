package com.ynova.medisalud.appointments.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.ynova.medisalud.appointments.domain.model.Patient;

public interface PatientRepositoryPort {

    Patient save(Patient patient);

    Optional<Patient> findById(Long id);

    List<Patient> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByDocumentId(String documentId);
}
