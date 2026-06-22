package com.ynova.medisalud.appointments.domain.model;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient validPatient() {
        return new Patient(null, "Juan Perez", "1234567890", "3001112233",
                "juan@mail.com", LocalDate.of(1990, 5, 15));
    }

    @Test
    void create_validPatient_allFieldsAssigned() {
        Patient p = validPatient();
        assertEquals("Juan Perez", p.getName());
        assertEquals("1234567890", p.getDocumentId());
        assertEquals("3001112233", p.getPhone());
        assertEquals("juan@mail.com", p.getEmail());
        assertEquals(LocalDate.of(1990, 5, 15), p.getBirthDate());
    }

    @Test
    void create_nullName_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, null, "1234567890", "3001112233", "j@m.com", null));
    }

    @Test
    void create_nameTooShort_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Ju", "1234567890", "3001112233", "j@m.com", null));
    }

    @Test
    void create_nullDocumentId_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", null, "3001112233", "j@m.com", null));
    }

    @Test
    void create_documentTooShort_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", "12345", "3001112233", "j@m.com", null));
    }

    @Test
    void create_documentExactly7_shouldSucceed() {
        Patient p = new Patient(null, "Juan Perez", "1234567", "3001112233", "j@m.com", null);
        assertEquals("1234567", p.getDocumentId());
    }

    @Test
    void create_nullPhone_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", "1234567890", null, "j@m.com", null));
    }

    @Test
    void create_invalidPhoneFormat_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", "1234567890", "abc1234", "j@m.com", null));
    }

    @Test
    void create_nullEmail_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", "1234567890", "3001112233", null, null));
    }

    @Test
    void create_invalidEmail_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", "1234567890", "3001112233", "invalid", null));
    }

    @Test
    void create_futureBirthDate_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", "1234567890", "3001112233",
                        "j@m.com", LocalDate.now().plusDays(1)));
    }

    @Test
    void create_todayBirthDate_shouldSucceed() {
        Patient p = new Patient(null, "Juan Perez", "1234567890", "3001112233",
                "j@m.com", LocalDate.now());
        assertEquals(LocalDate.now(), p.getBirthDate());
    }

    @Test
    void create_nullBirthDate_shouldSucceed() {
        Patient p = new Patient(null, "Juan Perez", "1234567890", "3001112233", "j@m.com", null);
        assertNull(p.getBirthDate());
    }

    @Test
    void create_trimFields_shouldTrim() {
        Patient p = new Patient(null, "  Juan Perez  ", " 1234567890 ", "3001112233", "j@m.com", null);
        assertEquals("Juan Perez", p.getName());
        assertEquals("1234567890", p.getDocumentId());
        assertEquals("j@m.com", p.getEmail());
    }
}
