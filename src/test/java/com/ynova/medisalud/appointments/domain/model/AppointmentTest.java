package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.ynova.medisalud.appointments.domain.exception.InvalidAppointmentStateException;
import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    private static final LocalDateTime FUTURE = LocalDateTime.of(2026, 7, 6, 10, 0);

    @Test
    void create_staticFactory_shouldSetDefaults() {
        Appointment apt = Appointment.create(1L, 2L, 3L, 10L, FUTURE);

        assertNull(apt.getId());
        assertEquals(1L, apt.getDoctorId());
        assertEquals(2L, apt.getPatientId());
        assertEquals(3L, apt.getSedeId());
        assertEquals(10L, apt.getSlotId());
        assertEquals(FUTURE, apt.getDateTime());
        assertEquals(AppointmentStatus.PROGRAMADA, apt.getStatus());
        assertNull(apt.getCancellationTimestamp());
        assertFalse(apt.isPenaltyApplied());
        assertEquals(0, apt.getVersion());
    }

    @Test
    void create_nullDoctorId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> Appointment.create(null, 2L, 3L, 10L, FUTURE));
    }

    @Test
    void create_nullPatientId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> Appointment.create(1L, null, 3L, 10L, FUTURE));
    }

    @Test
    void create_nullSedeId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> Appointment.create(1L, 2L, null, 10L, FUTURE));
    }

    @Test
    void create_nullDateTime_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> Appointment.create(1L, 2L, 3L, 10L, null));
    }

    @Test
    void cancel_programada_moreThan2Hours_noPenalty() {
        Appointment apt = Appointment.create(1L, 2L, 3L, 10L, FUTURE);
        LocalDateTime cancelTime = FUTURE.minusHours(5);

        boolean penalized = apt.cancel(cancelTime);

        assertFalse(penalized);
        assertEquals(AppointmentStatus.CANCELADA, apt.getStatus());
        assertEquals(cancelTime, apt.getCancellationTimestamp());
        assertFalse(apt.isPenaltyApplied());
    }

    @Test
    void cancel_programada_lessThan2Hours_withPenalty() {
        Appointment apt = Appointment.create(1L, 2L, 3L, 10L, FUTURE);
        LocalDateTime cancelTime = FUTURE.minusMinutes(30);

        boolean penalized = apt.cancel(cancelTime);

        assertTrue(penalized);
        assertEquals(AppointmentStatus.CANCELADA, apt.getStatus());
        assertTrue(apt.isPenaltyApplied());
    }

    @Test
    void cancel_programada_exactly2Hours_noPenalty() {
        Appointment apt = Appointment.create(1L, 2L, 3L, 10L, FUTURE);
        LocalDateTime cancelTime = FUTURE.minusHours(2);

        boolean penalized = apt.cancel(cancelTime);

        assertFalse(penalized);
        assertFalse(apt.isPenaltyApplied());
    }

    @Test
    void cancel_programada_1Hour59Min_withPenalty() {
        Appointment apt = Appointment.create(1L, 2L, 3L, 10L, FUTURE);
        LocalDateTime cancelTime = FUTURE.minusHours(1).minusMinutes(59);

        boolean penalized = apt.cancel(cancelTime);

        assertTrue(penalized);
        assertTrue(apt.isPenaltyApplied());
    }

    @Test
    void cancel_alreadyCancelled_shouldThrow() {
        Appointment apt = new Appointment(1L, 1L, 2L, 3L, 10L, FUTURE,
                AppointmentStatus.CANCELADA, LocalDateTime.now(), false, 0);

        assertThrows(InvalidAppointmentStateException.class,
                () -> apt.cancel(LocalDateTime.now()));
    }

    @Test
    void cancel_completada_shouldThrow() {
        Appointment apt = new Appointment(1L, 1L, 2L, 3L, 10L, FUTURE,
                AppointmentStatus.ATENDIDA, null, false, 0);

        assertThrows(InvalidAppointmentStateException.class,
                () -> apt.cancel(LocalDateTime.now()));
    }

    @Test
    void cancel_afterAppointmentTime_withPenalty() {
        Appointment apt = Appointment.create(1L, 2L, 3L, 10L, FUTURE);
        LocalDateTime cancelTime = FUTURE.plusMinutes(10);

        boolean penalized = apt.cancel(cancelTime);

        assertTrue(penalized);
    }
}
