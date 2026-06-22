package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Doctor;
import com.ynova.medisalud.appointments.domain.port.out.DoctorRepositoryPort;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper.DoctorPersistenceMapper;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository.JpaDoctorRepository;

@Component
public class DoctorRepositoryAdapter implements DoctorRepositoryPort {

    private final JpaDoctorRepository jpaRepository;
    private final DoctorPersistenceMapper mapper;

    public DoctorRepositoryAdapter(JpaDoctorRepository jpaRepository, DoctorPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Doctor save(Doctor doctor) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(doctor)));
    }

    @Override
    public Optional<Doctor> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Doctor> findAll() {
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
