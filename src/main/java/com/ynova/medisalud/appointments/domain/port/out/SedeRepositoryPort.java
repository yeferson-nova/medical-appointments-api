package com.ynova.medisalud.appointments.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.ynova.medisalud.appointments.domain.model.Sede;

public interface SedeRepositoryPort {

    Sede save(Sede sede);

    Optional<Sede> findById(Long id);

    List<Sede> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}
