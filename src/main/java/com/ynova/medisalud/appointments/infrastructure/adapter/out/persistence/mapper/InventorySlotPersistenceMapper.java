package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.InventorySlot;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.InventorySlotEntity;

@Component
public class InventorySlotPersistenceMapper {

    public InventorySlotEntity toEntity(InventorySlot domain) {
        InventorySlotEntity entity = new InventorySlotEntity();
        entity.setId(domain.getId());
        entity.setDoctorId(domain.getDoctorId());
        entity.setSedeId(domain.getSedeId());
        entity.setSlotDate(domain.getSlotDate());
        entity.setTimeStart(domain.getTimeStart());
        entity.setTimeEnd(domain.getTimeEnd());
        entity.setAvailable(domain.isAvailable());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    public InventorySlot toDomain(InventorySlotEntity entity) {
        return new InventorySlot(entity.getId(), entity.getDoctorId(), entity.getSedeId(),
                entity.getSlotDate(), entity.getTimeStart(), entity.getTimeEnd(),
                entity.isAvailable(), entity.getVersion() != null ? entity.getVersion() : 0);
    }
}
