package com.ynova.medisalud.appointments.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ynova.medisalud.appointments.domain.exception.DuplicateDocumentException;
import com.ynova.medisalud.appointments.domain.exception.ResourceNotFoundException;
import com.ynova.medisalud.appointments.domain.model.Patient;
import com.ynova.medisalud.appointments.domain.port.out.PatientRepositoryPort;

@Service
public class PatientService {

    private final PatientRepositoryPort patientRepository;

    public PatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient create(Patient patient) {
        if (patientRepository.existsByDocumentId(patient.getDocumentId())) {
            throw new DuplicateDocumentException(patient.getDocumentId());
        }
        return patientRepository.save(patient);
    }

    public Patient getById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Patient update(Long id, Patient patient) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        if (!existing.getDocumentId().equals(patient.getDocumentId())
                && patientRepository.existsByDocumentId(patient.getDocumentId())) {
            throw new DuplicateDocumentException(patient.getDocumentId());
        }

        Patient updated = new Patient(id, patient.getName(), patient.getDocumentId(),
                patient.getPhone(), patient.getEmail(), patient.getBirthDate());
        return patientRepository.save(updated);
    }

    public void delete(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente", id);
        }
        patientRepository.deleteById(id);
    }
}
