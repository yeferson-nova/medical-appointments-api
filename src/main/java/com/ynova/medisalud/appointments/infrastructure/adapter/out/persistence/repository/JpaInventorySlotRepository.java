package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.InventorySlotEntity;

public interface JpaInventorySlotRepository extends JpaRepository<InventorySlotEntity, Long> {

    Optional<InventorySlotEntity> findByDoctorIdAndSedeIdAndSlotDateAndTimeStart(
            Long doctorId, Long sedeId, LocalDate slotDate, LocalTime timeStart);

    List<InventorySlotEntity> findByDoctorIdAndSedeIdAndSlotDateBetweenAndAvailableTrue(
            Long doctorId, Long sedeId, LocalDate startDate, LocalDate endDate);

    boolean existsByDoctorIdAndSedeIdAndSlotDate(Long doctorId, Long sedeId, LocalDate slotDate);
}
