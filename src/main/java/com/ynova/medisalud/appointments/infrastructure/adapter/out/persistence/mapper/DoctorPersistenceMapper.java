package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Doctor;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.DoctorEntity;

@Component
public class DoctorPersistenceMapper {

    public DoctorEntity toEntity(Doctor domain) {
        DoctorEntity entity = new DoctorEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setSpecialty(domain.getSpecialty());
        entity.setPhone(domain.getPhone());
        entity.setEmail(domain.getEmail());
        return entity;
    }

    public Doctor toDomain(DoctorEntity entity) {
        return new Doctor(entity.getId(), entity.getName(), entity.getSpecialty(),
                entity.getPhone(), entity.getEmail());
    }
}
