package com.ynova.medisalud.appointments.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.ynova.medisalud.appointments.domain.model.Doctor;

public interface DoctorRepositoryPort {

    Doctor save(Doctor doctor);

    Optional<Doctor> findById(Long id);

    List<Doctor> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}
