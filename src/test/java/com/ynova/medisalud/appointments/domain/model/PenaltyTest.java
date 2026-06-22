package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

import static org.junit.jupiter.api.Assertions.*;

class PenaltyTest {

    @Test
    void create_validPenalty_allFieldsAssigned() {
        LocalDateTime now = LocalDateTime.now();
        Penalty p = Penalty.create(1L, 10L, now);

        assertNull(p.getId());
        assertEquals(1L, p.getPatientId());
        assertEquals(10L, p.getAppointmentId());
        assertEquals(now, p.getPenaltyDate());
    }

    @Test
    void create_nullPatientId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> Penalty.create(null, 10L, LocalDateTime.now()));
    }

    @Test
    void create_nullAppointmentId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> Penalty.create(1L, null, LocalDateTime.now()));
    }

    @Test
    void create_nullPenaltyDate_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> Penalty.create(1L, 10L, null));
    }

    @Test
    void constructor_withId_shouldAssign() {
        LocalDateTime now = LocalDateTime.now();
        Penalty p = new Penalty(5L, 1L, 10L, now);
        assertEquals(5L, p.getId());
    }
}
