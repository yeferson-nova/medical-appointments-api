package com.ynova.medisalud.appointments.domain.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ynova.medisalud.appointments.domain.exception.InvalidDomainFieldException;
import com.ynova.medisalud.appointments.domain.exception.ResourceNotFoundException;
import com.ynova.medisalud.appointments.domain.model.Doctor;
import com.ynova.medisalud.appointments.domain.port.out.DoctorRepositoryPort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock private DoctorRepositoryPort doctorRepo;

    private DoctorService service;

    @BeforeEach
    void setUp() {
        service = new DoctorService(doctorRepo);
    }

    @Test
    void create_validDoctor_shouldSucceed() {
        Doctor doctor = new Doctor(null, "Dr. Test", "Cardiologia", "5551001", "test@mail.com");
        when(doctorRepo.save(any())).thenReturn(new Doctor(1L, "Dr. Test", "Cardiologia", "5551001", "test@mail.com"));

        Doctor result = service.create(doctor);

        assertNotNull(result.getId());
        assertEquals("Dr. Test", result.getName());
    }

    @Test
    void create_invalidName_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, "Dr", "Cardiologia", null, null));
    }

    @Test
    void create_blankSpecialty_shouldThrow() {
        assertThrows(InvalidDomainFieldException.class,
                () -> new Doctor(null, "Dr. Test", "", null, null));
    }

    @Test
    void getById_existing_shouldReturn() {
        Doctor doctor = new Doctor(1L, "Dr. Test", "Cardiologia", null, null);
        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));

        assertEquals("Dr. Test", service.getById(1L).getName());
    }

    @Test
    void getById_notFound_shouldThrow() {
        when(doctorRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void delete_notFound_shouldThrow() {
        when(doctorRepo.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
    }
}
