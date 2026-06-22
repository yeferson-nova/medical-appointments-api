package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.adapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.InventorySlot;
import com.ynova.medisalud.appointments.domain.port.out.InventorySlotRepositoryPort;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper.InventorySlotPersistenceMapper;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository.JpaInventorySlotRepository;

@Component
public class InventorySlotRepositoryAdapter implements InventorySlotRepositoryPort {

    private final JpaInventorySlotRepository jpaRepository;
    private final InventorySlotPersistenceMapper mapper;

    public InventorySlotRepositoryAdapter(JpaInventorySlotRepository jpaRepository,
                                          InventorySlotPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public InventorySlot save(InventorySlot slot) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(slot)));
    }

    @Override
    public List<InventorySlot> saveAll(List<InventorySlot> slots) {
        var entities = slots.stream().map(mapper::toEntity).toList();
        return jpaRepository.saveAll(entities).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<InventorySlot> findByDoctorIdAndSedeIdAndDateAndTime(
            Long doctorId, Long sedeId, LocalDate date, LocalTime time) {
        return jpaRepository.findByDoctorIdAndSedeIdAndSlotDateAndTimeStart(doctorId, sedeId, date, time)
                .map(mapper::toDomain);
    }

    @Override
    public List<InventorySlot> findAvailableByDoctorAndSedeAndDateRange(
            Long doctorId, Long sedeId, LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByDoctorIdAndSedeIdAndSlotDateBetweenAndAvailableTrue(
                        doctorId, sedeId, startDate, endDate)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByDoctorIdAndSedeIdAndSlotDate(Long doctorId, Long sedeId, LocalDate date) {
        return jpaRepository.existsByDoctorIdAndSedeIdAndSlotDate(doctorId, sedeId, date);
    }
}
