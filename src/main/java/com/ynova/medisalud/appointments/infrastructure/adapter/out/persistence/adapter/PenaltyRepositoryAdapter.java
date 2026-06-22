package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.adapter;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Penalty;
import com.ynova.medisalud.appointments.domain.port.out.PenaltyRepositoryPort;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper.PenaltyPersistenceMapper;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository.JpaPenaltyRepository;

@Component
public class PenaltyRepositoryAdapter implements PenaltyRepositoryPort {

    private final JpaPenaltyRepository jpaRepository;
    private final PenaltyPersistenceMapper mapper;

    public PenaltyRepositoryAdapter(JpaPenaltyRepository jpaRepository, PenaltyPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Penalty save(Penalty penalty) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(penalty)));
    }

    @Override
    public long countByPatientIdAndDateAfter(Long patientId, LocalDateTime since) {
        return jpaRepository.countByPatientIdAndPenaltyDateAfter(patientId, since);
    }
}
