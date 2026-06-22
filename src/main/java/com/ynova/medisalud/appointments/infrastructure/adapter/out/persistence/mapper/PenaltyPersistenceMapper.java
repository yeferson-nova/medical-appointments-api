package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Penalty;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.PenaltyEntity;

@Component
public class PenaltyPersistenceMapper {

    public PenaltyEntity toEntity(Penalty domain) {
        PenaltyEntity entity = new PenaltyEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId());
        entity.setAppointmentId(domain.getAppointmentId());
        entity.setPenaltyDate(domain.getPenaltyDate());
        return entity;
    }

    public Penalty toDomain(PenaltyEntity entity) {
        return new Penalty(entity.getId(), entity.getPatientId(),
                entity.getAppointmentId(), entity.getPenaltyDate());
    }
}
