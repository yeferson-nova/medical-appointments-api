package com.ynova.medisalud.appointments.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.ynova.medisalud.appointments.domain.exception.InvalidScheduleException;
import com.ynova.medisalud.appointments.domain.model.TimeSlot;

import static org.junit.jupiter.api.Assertions.*;

class BusinessHoursValidatorTest {

    @Test
    void validate_weekdayValidSlot_shouldNotThrow() {
        LocalDateTime monday10am = LocalDateTime.of(2026, 7, 6, 10, 0);
        assertDoesNotThrow(() -> BusinessHoursValidator.validate(monday10am));
    }

    @Test
    void validate_weekdayLastSlot_shouldNotThrow() {
        LocalDateTime monday1730 = LocalDateTime.of(2026, 7, 6, 17, 30);
        assertDoesNotThrow(() -> BusinessHoursValidator.validate(monday1730));
    }

    @Test
    void validate_weekdayAt18_shouldThrow() {
        LocalDateTime monday18 = LocalDateTime.of(2026, 7, 6, 18, 0);
        assertThrows(InvalidScheduleException.class, () -> BusinessHoursValidator.validate(monday18));
    }

    @Test
    void validate_weekdayBefore8_shouldThrow() {
        LocalDateTime monday7am = LocalDateTime.of(2026, 7, 6, 7, 30);
        assertThrows(InvalidScheduleException.class, () -> BusinessHoursValidator.validate(monday7am));
    }

    @Test
    void validate_sunday_shouldThrow() {
        LocalDateTime sunday = LocalDateTime.of(2026, 7, 5, 10, 0);
        assertThrows(InvalidScheduleException.class, () -> BusinessHoursValidator.validate(sunday));
    }

    @Test
    void validate_saturday8am_shouldNotThrow() {
        LocalDateTime saturday8 = LocalDateTime.of(2026, 7, 4, 8, 0);
        assertDoesNotThrow(() -> BusinessHoursValidator.validate(saturday8));
    }

    @Test
    void validate_saturdayLastSlot1230_shouldNotThrow() {
        LocalDateTime saturday1230 = LocalDateTime.of(2026, 7, 4, 12, 30);
        assertDoesNotThrow(() -> BusinessHoursValidator.validate(saturday1230));
    }

    @Test
    void validate_saturdayAt13_shouldThrow() {
        LocalDateTime saturday13 = LocalDateTime.of(2026, 7, 4, 13, 0);
        assertThrows(InvalidScheduleException.class, () -> BusinessHoursValidator.validate(saturday13));
    }

    @Test
    void validate_invalidMinutes_shouldThrow() {
        LocalDateTime monday1015 = LocalDateTime.of(2026, 7, 6, 10, 15);
        assertThrows(InvalidScheduleException.class, () -> BusinessHoursValidator.validate(monday1015));
    }

    @Test
    void generateSlots_weekday_shouldReturn20Slots() {
        LocalDate monday = LocalDate.of(2026, 7, 6);
        List<TimeSlot> slots = BusinessHoursValidator.generateSlots(monday);
        assertEquals(20, slots.size());
        assertEquals(LocalDateTime.of(2026, 7, 6, 8, 0), slots.getFirst().start());
        assertEquals(LocalDateTime.of(2026, 7, 6, 17, 30), slots.getLast().start());
    }

    @Test
    void generateSlots_saturday_shouldReturn10Slots() {
        LocalDate saturday = LocalDate.of(2026, 7, 4);
        List<TimeSlot> slots = BusinessHoursValidator.generateSlots(saturday);
        assertEquals(10, slots.size());
        assertEquals(LocalDateTime.of(2026, 7, 4, 8, 0), slots.getFirst().start());
        assertEquals(LocalDateTime.of(2026, 7, 4, 12, 30), slots.getLast().start());
    }

    @Test
    void generateSlots_sunday_shouldReturnEmpty() {
        LocalDate sunday = LocalDate.of(2026, 7, 5);
        assertTrue(BusinessHoursValidator.generateSlots(sunday).isEmpty());
    }
}
