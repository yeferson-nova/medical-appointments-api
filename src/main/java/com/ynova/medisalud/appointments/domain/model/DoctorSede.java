package com.ynova.medisalud.appointments.domain.model;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

public class DoctorSede {

    private final Long id;
    private final Long doctorId;
    private final Long sedeId;

    public DoctorSede(Long id, Long doctorId, Long sedeId) {
        if (doctorId == null) {
            throw new InvalidDomainFieldException("doctorId", "El id del medico es obligatorio");
        }
        if (sedeId == null) {
            throw new InvalidDomainFieldException("sedeId", "El id de la sede es obligatorio");
        }
        this.id = id;
        this.doctorId = doctorId;
        this.sedeId = sedeId;
    }

    public Long getId() { return id; }
    public Long getDoctorId() { return doctorId; }
    public Long getSedeId() { return sedeId; }
}
