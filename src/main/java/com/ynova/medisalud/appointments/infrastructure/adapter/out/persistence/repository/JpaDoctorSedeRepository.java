package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.DoctorEntity;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.DoctorSedeEntity;

public interface JpaDoctorSedeRepository extends JpaRepository<DoctorSedeEntity, Long> {

    boolean existsByDoctorIdAndSedeId(Long doctorId, Long sedeId);

    @Query("SELECT d FROM DoctorEntity d WHERE d.id IN (SELECT ds.doctorId FROM DoctorSedeEntity ds WHERE ds.sedeId = :sedeId)")
    List<DoctorEntity> findDoctorsBySedeId(Long sedeId);

    void deleteByDoctorIdAndSedeId(Long doctorId, Long sedeId);
}
