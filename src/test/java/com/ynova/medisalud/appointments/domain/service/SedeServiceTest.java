package com.ynova.medisalud.appointments.domain.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ynova.medisalud.appointments.domain.exception.ResourceNotFoundException;
import com.ynova.medisalud.appointments.domain.model.Doctor;
import com.ynova.medisalud.appointments.domain.model.DoctorSede;
import com.ynova.medisalud.appointments.domain.model.Sede;
import com.ynova.medisalud.appointments.domain.port.out.DoctorRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.DoctorSedeRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.SedeRepositoryPort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SedeServiceTest {

    @Mock private SedeRepositoryPort sedeRepo;
    @Mock private DoctorRepositoryPort doctorRepo;
    @Mock private DoctorSedeRepositoryPort doctorSedeRepo;

    private SedeService service;

    @BeforeEach
    void setUp() {
        service = new SedeService(sedeRepo, doctorRepo, doctorSedeRepo);
    }

    @Test
    void create_validSede_shouldSucceed() {
        Sede sede = new Sede(null, "Sede Norte", "Calle 100", "Bogota", "6011234567");
        when(sedeRepo.save(any())).thenReturn(new Sede(1L, "Sede Norte", "Calle 100", "Bogota", "6011234567"));

        Sede result = service.create(sede);

        assertNotNull(result.getId());
        assertEquals("Sede Norte", result.getName());
    }

    @Test
    void getById_existing_shouldReturn() {
        Sede sede = new Sede(1L, "Sede Norte", "Calle 100", "Bogota", null);
        when(sedeRepo.findById(1L)).thenReturn(Optional.of(sede));

        assertEquals("Sede Norte", service.getById(1L).getName());
    }

    @Test
    void getById_notFound_shouldThrow() {
        when(sedeRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void getAll_shouldReturnList() {
        when(sedeRepo.findAll()).thenReturn(List.of(
                new Sede(1L, "Sede Norte", "Calle 100", "Bogota", null),
                new Sede(2L, "Sede Centro", "Carrera 7", "Bogota", null)));

        assertEquals(2, service.getAll().size());
    }

    @Test
    void update_existing_shouldSucceed() {
        when(sedeRepo.existsById(1L)).thenReturn(true);
        Sede updated = new Sede(1L, "Sede Norte Actualizada", "Calle 200", "Medellin", null);
        when(sedeRepo.save(any())).thenReturn(updated);

        Sede result = service.update(1L, updated);
        assertEquals("Sede Norte Actualizada", result.getName());
    }

    @Test
    void update_notFound_shouldThrow() {
        when(sedeRepo.existsById(99L)).thenReturn(false);
        Sede sede = new Sede(99L, "Test", "Test", "Test", null);
        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, sede));
    }

    @Test
    void delete_existing_shouldSucceed() {
        when(sedeRepo.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> service.delete(1L));
        verify(sedeRepo).deleteById(1L);
    }

    @Test
    void delete_notFound_shouldThrow() {
        when(sedeRepo.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
    }

    @Test
    void assignDoctor_validIds_shouldSucceed() {
        when(sedeRepo.existsById(1L)).thenReturn(true);
        when(doctorRepo.existsById(2L)).thenReturn(true);
        when(doctorSedeRepo.save(any())).thenReturn(new DoctorSede(1L, 2L, 1L));

        DoctorSede result = service.assignDoctor(1L, 2L);

        assertEquals(2L, result.getDoctorId());
        assertEquals(1L, result.getSedeId());
    }

    @Test
    void assignDoctor_sedeNotFound_shouldThrow() {
        when(sedeRepo.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.assignDoctor(99L, 1L));
    }

    @Test
    void assignDoctor_doctorNotFound_shouldThrow() {
        when(sedeRepo.existsById(1L)).thenReturn(true);
        when(doctorRepo.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.assignDoctor(1L, 99L));
    }

    @Test
    void getDoctorsBySede_existing_shouldReturnDoctors() {
        when(sedeRepo.existsById(1L)).thenReturn(true);
        when(doctorSedeRepo.findDoctorsBySedeId(1L)).thenReturn(List.of(
                new Doctor(1L, "Dr. Test", "Cardiologia", null, null)));

        List<Doctor> doctors = service.getDoctorsBySede(1L);
        assertEquals(1, doctors.size());
    }

    @Test
    void getDoctorsBySede_sedeNotFound_shouldThrow() {
        when(sedeRepo.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.getDoctorsBySede(99L));
    }

    @Test
    void unassignDoctor_sedeNotFound_shouldThrow() {
        when(sedeRepo.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.unassignDoctor(99L, 1L));
    }

    @Test
    void unassignDoctor_existing_shouldCallDelete() {
        when(sedeRepo.existsById(1L)).thenReturn(true);
        service.unassignDoctor(1L, 2L);
        verify(doctorSedeRepo).deleteByDoctorIdAndSedeId(2L, 1L);
    }
}
