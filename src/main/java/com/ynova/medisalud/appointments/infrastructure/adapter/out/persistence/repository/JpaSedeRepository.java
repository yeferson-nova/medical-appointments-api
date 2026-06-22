package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.SedeEntity;

public interface JpaSedeRepository extends JpaRepository<SedeEntity, Long> {
}
