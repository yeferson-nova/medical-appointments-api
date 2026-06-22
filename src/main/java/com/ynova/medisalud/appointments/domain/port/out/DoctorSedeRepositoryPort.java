package com.ynova.medisalud.appointments.domain.port.out;

import java.util.List;

import com.ynova.medisalud.appointments.domain.model.Doctor;
import com.ynova.medisalud.appointments.domain.model.DoctorSede;

public interface DoctorSedeRepositoryPort {

    DoctorSede save(DoctorSede doctorSede);

    boolean existsByDoctorIdAndSedeId(Long doctorId, Long sedeId);

    List<Doctor> findDoctorsBySedeId(Long sedeId);

    void deleteByDoctorIdAndSedeId(Long doctorId, Long sedeId);
}
