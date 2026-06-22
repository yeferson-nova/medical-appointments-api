package com.ynova.medisalud.appointments.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.ynova.medisalud.appointments.domain.model.Appointment;
import com.ynova.medisalud.appointments.domain.model.AppointmentFilter;

public interface AppointmentRepositoryPort {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(Long id);

    List<Appointment> findByFilter(AppointmentFilter filter);
}
