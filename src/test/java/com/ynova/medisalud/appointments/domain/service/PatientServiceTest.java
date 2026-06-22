package com.ynova.medisalud.appointments.domain.service;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ynova.medisalud.appointments.domain.exception.DuplicateDocumentException;
import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;
import com.ynova.medisalud.appointments.domain.exception.ResourceNotFoundException;
import com.ynova.medisalud.appointments.domain.model.Patient;
import com.ynova.medisalud.appointments.domain.port.out.PatientRepositoryPort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock private PatientRepositoryPort patientRepo;

    private PatientService service;

    @BeforeEach
    void setUp() {
        service = new PatientService(patientRepo);
    }

    @Test
    void create_validPatient_shouldSucceed() {
        Patient patient = new Patient(null, "Juan Perez", "1234567890", "3001112233",
                "juan@mail.com", LocalDate.of(1990, 5, 15));
        when(patientRepo.existsByDocumentId("1234567890")).thenReturn(false);
        when(patientRepo.save(any())).thenReturn(
                new Patient(1L, "Juan Perez", "1234567890", "3001112233",
                        "juan@mail.com", LocalDate.of(1990, 5, 15)));

        Patient result = service.create(patient);
        assertNotNull(result.getId());
    }

    @Test
    void create_duplicateDocument_shouldThrow() {
        Patient patient = new Patient(null, "Juan Perez", "1234567890", "3001112233",
                "juan@mail.com", null);
        when(patientRepo.existsByDocumentId("1234567890")).thenReturn(true);

        assertThrows(DuplicateDocumentException.class, () -> service.create(patient));
    }

    @Test
    void create_futureBirthDate_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", "1234567890", "3001112233",
                        "juan@mail.com", LocalDate.now().plusDays(1)));
    }

    @Test
    void create_shortDocument_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", "12345", "3001112233",
                        "juan@mail.com", null));
    }

    @Test
    void create_invalidEmail_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Patient(null, "Juan Perez", "1234567890", "3001112233",
                        "not-an-email", null));
    }

    @Test
    void getById_notFound_shouldThrow() {
        when(patientRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }
}
