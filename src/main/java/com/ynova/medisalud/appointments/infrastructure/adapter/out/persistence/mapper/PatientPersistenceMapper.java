package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Patient;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.PatientEntity;

@Component
public class PatientPersistenceMapper {

    public PatientEntity toEntity(Patient domain) {
        PatientEntity entity = new PatientEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDocumentId(domain.getDocumentId());
        entity.setPhone(domain.getPhone());
        entity.setEmail(domain.getEmail());
        entity.setBirthDate(domain.getBirthDate());
        return entity;
    }

    public Patient toDomain(PatientEntity entity) {
        return new Patient(entity.getId(), entity.getName(), entity.getDocumentId(),
                entity.getPhone(), entity.getEmail(), entity.getBirthDate());
    }
}
