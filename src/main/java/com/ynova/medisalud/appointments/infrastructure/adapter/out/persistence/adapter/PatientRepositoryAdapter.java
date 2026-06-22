package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Patient;
import com.ynova.medisalud.appointments.domain.port.out.PatientRepositoryPort;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper.PatientPersistenceMapper;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository.JpaPatientRepository;

@Component
public class PatientRepositoryAdapter implements PatientRepositoryPort {

    private final JpaPatientRepository jpaRepository;
    private final PatientPersistenceMapper mapper;

    public PatientRepositoryAdapter(JpaPatientRepository jpaRepository, PatientPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Patient save(Patient patient) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(patient)));
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByDocumentId(String documentId) {
        return jpaRepository.existsByDocumentId(documentId);
    }
}
