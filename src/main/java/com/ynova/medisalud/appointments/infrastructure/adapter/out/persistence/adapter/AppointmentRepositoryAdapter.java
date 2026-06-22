package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Appointment;
import com.ynova.medisalud.appointments.domain.model.AppointmentFilter;
import com.ynova.medisalud.appointments.domain.port.out.AppointmentRepositoryPort;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.AppointmentEntity;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper.AppointmentPersistenceMapper;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository.AppointmentSpecification;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository.JpaAppointmentRepository;

@Component
public class AppointmentRepositoryAdapter implements AppointmentRepositoryPort {

    private final JpaAppointmentRepository jpaRepository;
    private final AppointmentPersistenceMapper mapper;

    public AppointmentRepositoryAdapter(JpaAppointmentRepository jpaRepository,
                                        AppointmentPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Appointment save(Appointment appointment) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(appointment)));
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Appointment> findByFilter(AppointmentFilter filter) {
        Specification<AppointmentEntity> spec = AppointmentSpecification.fromFilter(filter);
        return jpaRepository.findAll(spec).stream().map(mapper::toDomain).toList();
    }
}
