package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.DoctorEntity;

public interface JpaDoctorRepository extends JpaRepository<DoctorEntity, Long> {
}
