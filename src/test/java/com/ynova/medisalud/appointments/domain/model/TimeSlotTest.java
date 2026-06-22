package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    void of_validStart_shouldSetEndTo30MinLater() {
        LocalDateTime start = LocalDateTime.of(2026, 7, 6, 10, 0);
        TimeSlot slot = TimeSlot.of(start);

        assertEquals(start, slot.start());
        assertEquals(start.plusMinutes(30), slot.end());
    }

    @Test
    void of_nullStart_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> TimeSlot.of(null));
    }

    @Test
    void of_halfHourStart_shouldSetEndCorrectly() {
        LocalDateTime start = LocalDateTime.of(2026, 7, 6, 10, 30);
        TimeSlot slot = TimeSlot.of(start);

        assertEquals(LocalDateTime.of(2026, 7, 6, 11, 0), slot.end());
    }
}
