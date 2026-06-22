package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Sede;
import com.ynova.medisalud.appointments.domain.port.out.SedeRepositoryPort;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper.SedePersistenceMapper;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository.JpaSedeRepository;

@Component
public class SedeRepositoryAdapter implements SedeRepositoryPort {

    private final JpaSedeRepository jpaRepository;
    private final SedePersistenceMapper mapper;

    public SedeRepositoryAdapter(JpaSedeRepository jpaRepository, SedePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Sede save(Sede sede) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(sede)));
    }

    @Override
    public Optional<Sede> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Sede> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
