package com.ynova.medisalud.appointments.domain.model;

import org.junit.jupiter.api.Test;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    @Test
    void create_validDoctor_allFieldsAssigned() {
        Doctor doctor = new Doctor(1L, "Dra. Maria Gonzalez", "Cardiologia", "5551001", "maria@mail.com");

        assertEquals(1L, doctor.getId());
        assertEquals("Dra. Maria Gonzalez", doctor.getName());
        assertEquals("Cardiologia", doctor.getSpecialty());
        assertEquals("5551001", doctor.getPhone());
        assertEquals("maria@mail.com", doctor.getEmail());
    }

    @Test
    void create_nullName_shouldThrow() {
        InvalidDomainFieldException ex = assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, null, "Cardiologia", null, null));
        assertEquals("name", ex.getField());
    }

    @Test
    void create_blankName_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, "  ", "Cardiologia", null, null));
    }

    @Test
    void create_nameTooShort_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, "Dr", "Cardiologia", null, null));
    }

    @Test
    void create_nameTooLong_shouldThrow() {
        String longName = "A".repeat(101);
        assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, longName, "Cardiologia", null, null));
    }

    @Test
    void create_nameExactly3Chars_shouldSucceed() {
        Doctor doctor = new Doctor(null, "Ana", "Pediatria", null, null);
        assertEquals("Ana", doctor.getName());
    }

    @Test
    void create_nameExactly100Chars_shouldSucceed() {
        String name = "A".repeat(100);
        Doctor doctor = new Doctor(null, name, "Pediatria", null, null);
        assertEquals(100, doctor.getName().length());
    }

    @Test
    void create_nullSpecialty_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, "Dr. Test", null, null, null));
    }

    @Test
    void create_blankSpecialty_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, "Dr. Test", "", null, null));
    }

    @Test
    void create_invalidPhone_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, "Dr. Test", "Cardiologia", "123", null));
    }

    @Test
    void create_validPhoneMinDigits_shouldSucceed() {
        Doctor doctor = new Doctor(null, "Dr. Test", "Cardiologia", "1234567", null);
        assertEquals("1234567", doctor.getPhone());
    }

    @Test
    void create_nullPhone_shouldSucceed() {
        Doctor doctor = new Doctor(null, "Dr. Test", "Cardiologia", null, null);
        assertNull(doctor.getPhone());
    }

    @Test
    void create_invalidEmail_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, "Dr. Test", "Cardiologia", null, "not-email"));
    }

    @Test
    void create_validEmail_shouldSucceed() {
        Doctor doctor = new Doctor(null, "Dr. Test", "Cardiologia", null, "doc@hospital.com");
        assertEquals("doc@hospital.com", doctor.getEmail());
    }

    @Test
    void create_nullEmail_shouldSucceed() {
        Doctor doctor = new Doctor(null, "Dr. Test", "Cardiologia", null, null);
        assertNull(doctor.getEmail());
    }

    @Test
    void create_trimName_shouldTrim() {
        Doctor doctor = new Doctor(null, "  Dr. Test  ", "  Cardiologia  ", null, null);
        assertEquals("Dr. Test", doctor.getName());
        assertEquals("Cardiologia", doctor.getSpecialty());
    }
}
