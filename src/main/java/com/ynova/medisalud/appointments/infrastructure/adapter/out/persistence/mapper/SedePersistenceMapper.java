package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Sede;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.SedeEntity;

@Component
public class SedePersistenceMapper {

    public SedeEntity toEntity(Sede domain) {
        SedeEntity entity = new SedeEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setAddress(domain.getAddress());
        entity.setCity(domain.getCity());
        entity.setPhone(domain.getPhone());
        return entity;
    }

    public Sede toDomain(SedeEntity entity) {
        return new Sede(entity.getId(), entity.getName(), entity.getAddress(),
                entity.getCity(), entity.getPhone());
    }
}
