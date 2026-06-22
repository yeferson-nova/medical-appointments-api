package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ynova.medisalud.appointments.domain.model.Doctor;
import com.ynova.medisalud.appointments.domain.model.DoctorSede;
import com.ynova.medisalud.appointments.domain.port.out.DoctorSedeRepositoryPort;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.DoctorSedeEntity;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.mapper.DoctorPersistenceMapper;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository.JpaDoctorSedeRepository;

@Component
public class DoctorSedeRepositoryAdapter implements DoctorSedeRepositoryPort {

    private final JpaDoctorSedeRepository jpaRepository;
    private final DoctorPersistenceMapper doctorMapper;

    public DoctorSedeRepositoryAdapter(JpaDoctorSedeRepository jpaRepository,
                                       DoctorPersistenceMapper doctorMapper) {
        this.jpaRepository = jpaRepository;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public DoctorSede save(DoctorSede doctorSede) {
        DoctorSedeEntity entity = new DoctorSedeEntity();
        entity.setDoctorId(doctorSede.getDoctorId());
        entity.setSedeId(doctorSede.getSedeId());
        DoctorSedeEntity saved = jpaRepository.save(entity);
        return new DoctorSede(saved.getId(), saved.getDoctorId(), saved.getSedeId());
    }

    @Override
    public boolean existsByDoctorIdAndSedeId(Long doctorId, Long sedeId) {
        return jpaRepository.existsByDoctorIdAndSedeId(doctorId, sedeId);
    }

    @Override
    public List<Doctor> findDoctorsBySedeId(Long sedeId) {
        return jpaRepository.findDoctorsBySedeId(sedeId).stream()
                .map(doctorMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteByDoctorIdAndSedeId(Long doctorId, Long sedeId) {
        jpaRepository.deleteByDoctorIdAndSedeId(doctorId, sedeId);
    }
}
