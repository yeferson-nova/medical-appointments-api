package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.AppointmentEntity;

public interface JpaAppointmentRepository extends JpaRepository<AppointmentEntity, Long>,
        JpaSpecificationExecutor<AppointmentEntity> {
}
