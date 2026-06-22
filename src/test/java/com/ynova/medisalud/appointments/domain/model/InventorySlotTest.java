package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

import static org.junit.jupiter.api.Assertions.*;

class InventorySlotTest {

    private InventorySlot validSlot() {
        return new InventorySlot(1L, 1L, 1L, LocalDate.of(2026, 7, 6),
                LocalTime.of(10, 0), LocalTime.of(10, 30), true, 0);
    }

    @Test
    void create_validSlot_allFieldsAssigned() {
        InventorySlot slot = validSlot();
        assertEquals(1L, slot.getId());
        assertEquals(1L, slot.getDoctorId());
        assertEquals(1L, slot.getSedeId());
        assertEquals(LocalDate.of(2026, 7, 6), slot.getSlotDate());
        assertEquals(LocalTime.of(10, 0), slot.getTimeStart());
        assertEquals(LocalTime.of(10, 30), slot.getTimeEnd());
        assertTrue(slot.isAvailable());
    }

    @Test
    void create_nullDoctorId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new InventorySlot(null, null, 1L, LocalDate.now(),
                        LocalTime.of(10, 0), LocalTime.of(10, 30), true, 0));
    }

    @Test
    void create_nullSedeId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new InventorySlot(null, 1L, null, LocalDate.now(),
                        LocalTime.of(10, 0), LocalTime.of(10, 30), true, 0));
    }

    @Test
    void create_nullDate_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new InventorySlot(null, 1L, 1L, null,
                        LocalTime.of(10, 0), LocalTime.of(10, 30), true, 0));
    }

    @Test
    void create_nullTimeStart_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new InventorySlot(null, 1L, 1L, LocalDate.now(),
                        null, LocalTime.of(10, 30), true, 0));
    }

    @Test
    void create_nullTimeEnd_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new InventorySlot(null, 1L, 1L, LocalDate.now(),
                        LocalTime.of(10, 0), null, true, 0));
    }

    @Test
    void reserve_availableSlot_shouldBecomeUnavailable() {
        InventorySlot slot = validSlot();
        assertTrue(slot.isAvailable());

        slot.reserve();

        assertFalse(slot.isAvailable());
    }

    @Test
    void reserve_alreadyReserved_shouldThrow() {
        InventorySlot slot = new InventorySlot(1L, 1L, 1L, LocalDate.of(2026, 7, 6),
                LocalTime.of(10, 0), LocalTime.of(10, 30), false, 0);

        assertThrows(RuntimeException.class, () -> slot.reserve());
    }

    @Test
    void release_reservedSlot_shouldBecomeAvailable() {
        InventorySlot slot = new InventorySlot(1L, 1L, 1L, LocalDate.of(2026, 7, 6),
                LocalTime.of(10, 0), LocalTime.of(10, 30), false, 0);
        assertFalse(slot.isAvailable());

        slot.release();

        assertTrue(slot.isAvailable());
    }
}
