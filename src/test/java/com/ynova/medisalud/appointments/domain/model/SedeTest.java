package com.ynova.medisalud.appointments.domain.model;

import org.junit.jupiter.api.Test;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

import static org.junit.jupiter.api.Assertions.*;

class SedeTest {

    @Test
    void create_validSede_allFieldsAssigned() {
        Sede sede = new Sede(1L, "Sede Norte", "Calle 100", "Bogota", "6011234567");
        assertEquals(1L, sede.getId());
        assertEquals("Sede Norte", sede.getName());
        assertEquals("Calle 100", sede.getAddress());
        assertEquals("Bogota", sede.getCity());
        assertEquals("6011234567", sede.getPhone());
    }

    @Test
    void create_nullName_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Sede(null, null, "Calle 100", "Bogota", null));
    }

    @Test
    void create_blankName_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Sede(null, "  ", "Calle 100", "Bogota", null));
    }

    @Test
    void create_nameTooShort_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Sede(null, "AB", "Calle 100", "Bogota", null));
    }

    @Test
    void create_nullAddress_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Sede(null, "Sede Norte", null, "Bogota", null));
    }

    @Test
    void create_nullCity_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Sede(null, "Sede Norte", "Calle 100", null, null));
    }

    @Test
    void create_nullPhone_shouldSucceed() {
        Sede sede = new Sede(null, "Sede Norte", "Calle 100", "Bogota", null);
        assertNull(sede.getPhone());
    }

    @Test
    void create_trimFields() {
        Sede sede = new Sede(null, "  Sede Norte  ", "  Calle 100  ", "  Bogota  ", null);
        assertEquals("Sede Norte", sede.getName());
        assertEquals("Calle 100", sede.getAddress());
        assertEquals("Bogota", sede.getCity());
    }
}
