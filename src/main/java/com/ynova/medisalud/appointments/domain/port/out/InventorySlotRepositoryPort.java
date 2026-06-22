package com.ynova.medisalud.appointments.domain.port.out;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.ynova.medisalud.appointments.domain.model.InventorySlot;

public interface InventorySlotRepositoryPort {

    InventorySlot save(InventorySlot slot);

    List<InventorySlot> saveAll(List<InventorySlot> slots);

    Optional<InventorySlot> findByDoctorIdAndSedeIdAndDateAndTime(
            Long doctorId, Long sedeId, LocalDate date, LocalTime time);

    List<InventorySlot> findAvailableByDoctorAndSedeAndDateRange(
            Long doctorId, Long sedeId, LocalDate startDate, LocalDate endDate);

    boolean existsByDoctorIdAndSedeIdAndSlotDate(Long doctorId, Long sedeId, LocalDate date);
}
