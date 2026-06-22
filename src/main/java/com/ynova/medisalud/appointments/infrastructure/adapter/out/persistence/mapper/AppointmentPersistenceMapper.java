package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Appointment;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.AppointmentEntity;

@Component
public class AppointmentPersistenceMapper {

    public AppointmentEntity toEntity(Appointment domain) {
        AppointmentEntity entity = new AppointmentEntity();
        entity.setId(domain.getId());
        entity.setDoctorId(domain.getDoctorId());
        entity.setPatientId(domain.getPatientId());
        entity.setSedeId(domain.getSedeId());
        entity.setSlotId(domain.getSlotId());
        entity.setDateTime(domain.getDateTime());
        entity.setStatus(domain.getStatus());
        entity.setCancellationTimestamp(domain.getCancellationTimestamp());
        entity.setPenaltyApplied(domain.isPenaltyApplied());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    public Appointment toDomain(AppointmentEntity entity) {
        return new Appointment(entity.getId(), entity.getDoctorId(), entity.getPatientId(),
                entity.getSedeId(), entity.getSlotId(), entity.getDateTime(),
                entity.getStatus(), entity.getCancellationTimestamp(),
                entity.isPenaltyApplied(), entity.getVersion() != null ? entity.getVersion() : 0);
    }
}
