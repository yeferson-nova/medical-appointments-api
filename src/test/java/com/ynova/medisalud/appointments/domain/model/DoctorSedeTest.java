package com.ynova.medisalud.appointments.domain.model;

import org.junit.jupiter.api.Test;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

import static org.junit.jupiter.api.Assertions.*;

class DoctorSedeTest {

    @Test
    void create_validDoctorSede_shouldAssignFields() {
        DoctorSede ds = new DoctorSede(1L, 2L, 3L);
        assertEquals(1L, ds.getId());
        assertEquals(2L, ds.getDoctorId());
        assertEquals(3L, ds.getSedeId());
    }

    @Test
    void create_nullDoctorId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new DoctorSede(null, null, 3L));
    }

    @Test
    void create_nullSedeId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new DoctorSede(null, 2L, null));
    }
}
