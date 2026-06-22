package com.ynova.medisalud.appointments.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.ynova.medisalud.appointments.domain.exception.InvalidAppointmentStateException;
import com.ynova.medisalud.appointments.domain.exception.InvalidScheduleException;
import com.ynova.medisalud.appointments.domain.exception.PatientBlockedException;
import com.ynova.medisalud.appointments.domain.exception.ResourceNotFoundException;
import com.ynova.medisalud.appointments.domain.event.AppointmentBookedEvent;
import com.ynova.medisalud.appointments.domain.exception.SlotNotAvailableException;
import com.ynova.medisalud.appointments.domain.model.Appointment;
import com.ynova.medisalud.appointments.domain.model.AppointmentStatus;
import com.ynova.medisalud.appointments.domain.model.InventorySlot;
import com.ynova.medisalud.appointments.domain.port.out.AppointmentRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.DoctorRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.DoctorSedeRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.InventorySlotRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.PatientRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.PenaltyRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.SedeRepositoryPort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock private AppointmentRepositoryPort appointmentRepo;
    @Mock private InventorySlotRepositoryPort slotRepo;
    @Mock private DoctorRepositoryPort doctorRepo;
    @Mock private PatientRepositoryPort patientRepo;
    @Mock private SedeRepositoryPort sedeRepo;
    @Mock private DoctorSedeRepositoryPort doctorSedeRepo;
    @Mock private PenaltyRepositoryPort penaltyRepo;
    @Mock private ApplicationEventPublisher eventPublisher;

    private AppointmentService service;

    private static final Long DOCTOR_ID = 1L;
    private static final Long PATIENT_ID = 1L;
    private static final Long SEDE_ID = 1L;
    private static final LocalDateTime VALID_MONDAY_10AM = LocalDateTime.of(2026, 7, 6, 10, 0);

    @BeforeEach
    void setUp() {
        service = new AppointmentService(appointmentRepo, slotRepo, doctorRepo, patientRepo,
                sedeRepo, doctorSedeRepo, penaltyRepo, eventPublisher);
    }

    private void setupValidBookingMocks() {
        when(sedeRepo.existsById(SEDE_ID)).thenReturn(true);
        when(doctorRepo.existsById(DOCTOR_ID)).thenReturn(true);
        when(doctorSedeRepo.existsByDoctorIdAndSedeId(DOCTOR_ID, SEDE_ID)).thenReturn(true);
        when(patientRepo.existsById(PATIENT_ID)).thenReturn(true);
        when(penaltyRepo.countByPatientIdAndDateAfter(eq(PATIENT_ID), any())).thenReturn(0L);

        InventorySlot availableSlot = new InventorySlot(
                10L, DOCTOR_ID, SEDE_ID, VALID_MONDAY_10AM.toLocalDate(),
                LocalTime.of(10, 0), LocalTime.of(10, 30), true, 0);
        when(slotRepo.findByDoctorIdAndSedeIdAndDateAndTime(DOCTOR_ID, SEDE_ID,
                VALID_MONDAY_10AM.toLocalDate(), VALID_MONDAY_10AM.toLocalTime()))
                .thenReturn(Optional.of(availableSlot));
        when(slotRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        when(appointmentRepo.save(any())).thenAnswer(inv -> {
            Appointment apt = inv.getArgument(0);
            return new Appointment(42L, apt.getDoctorId(), apt.getPatientId(),
                    apt.getSedeId(), apt.getSlotId(), apt.getDateTime(),
                    apt.getStatus(), apt.getCancellationTimestamp(),
                    apt.isPenaltyApplied(), apt.getVersion());
        });
    }

    @Test
    void book_validRequest_shouldCreateAppointment() {
        setupValidBookingMocks();

        Appointment result = service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, VALID_MONDAY_10AM);

        assertNotNull(result);
        assertEquals(42L, result.getId());
        assertEquals(AppointmentStatus.PROGRAMADA, result.getStatus());
        verify(eventPublisher).publishEvent(any(AppointmentBookedEvent.class));
    }

    @Test
    void book_sunday_shouldThrowInvalidSchedule() {
        LocalDateTime sunday = LocalDateTime.of(2026, 7, 5, 10, 0);
        assertThrows(InvalidScheduleException.class,
                () -> service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, sunday));
    }

    @Test
    void book_beforeBusinessHours_shouldThrow() {
        LocalDateTime early = LocalDateTime.of(2026, 7, 6, 7, 0);
        assertThrows(InvalidScheduleException.class,
                () -> service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, early));
    }

    @Test
    void book_sedeNotFound_shouldThrow() {
        when(sedeRepo.existsById(SEDE_ID)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,
                () -> service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, VALID_MONDAY_10AM));
    }

    @Test
    void book_doctorNotFound_shouldThrow() {
        when(sedeRepo.existsById(SEDE_ID)).thenReturn(true);
        when(doctorRepo.existsById(DOCTOR_ID)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,
                () -> service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, VALID_MONDAY_10AM));
    }

    @Test
    void book_doctorNotInSede_shouldThrow() {
        when(sedeRepo.existsById(SEDE_ID)).thenReturn(true);
        when(doctorRepo.existsById(DOCTOR_ID)).thenReturn(true);
        when(doctorSedeRepo.existsByDoctorIdAndSedeId(DOCTOR_ID, SEDE_ID)).thenReturn(false);
        assertThrows(InvalidScheduleException.class,
                () -> service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, VALID_MONDAY_10AM));
    }

    @Test
    void book_patientNotFound_shouldThrow() {
        when(sedeRepo.existsById(SEDE_ID)).thenReturn(true);
        when(doctorRepo.existsById(DOCTOR_ID)).thenReturn(true);
        when(doctorSedeRepo.existsByDoctorIdAndSedeId(DOCTOR_ID, SEDE_ID)).thenReturn(true);
        when(patientRepo.existsById(PATIENT_ID)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,
                () -> service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, VALID_MONDAY_10AM));
    }

    @Test
    void book_patientBlockedWith3Penalties_shouldThrow() {
        when(sedeRepo.existsById(SEDE_ID)).thenReturn(true);
        when(doctorRepo.existsById(DOCTOR_ID)).thenReturn(true);
        when(doctorSedeRepo.existsByDoctorIdAndSedeId(DOCTOR_ID, SEDE_ID)).thenReturn(true);
        when(patientRepo.existsById(PATIENT_ID)).thenReturn(true);
        when(penaltyRepo.countByPatientIdAndDateAfter(eq(PATIENT_ID), any())).thenReturn(3L);

        assertThrows(PatientBlockedException.class,
                () -> service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, VALID_MONDAY_10AM));
    }

    @Test
    void book_patientWith2Penalties_shouldSucceed() {
        when(sedeRepo.existsById(SEDE_ID)).thenReturn(true);
        when(doctorRepo.existsById(DOCTOR_ID)).thenReturn(true);
        when(doctorSedeRepo.existsByDoctorIdAndSedeId(DOCTOR_ID, SEDE_ID)).thenReturn(true);
        when(patientRepo.existsById(PATIENT_ID)).thenReturn(true);
        when(penaltyRepo.countByPatientIdAndDateAfter(eq(PATIENT_ID), any())).thenReturn(2L);

        InventorySlot slot = new InventorySlot(10L, DOCTOR_ID, SEDE_ID,
                VALID_MONDAY_10AM.toLocalDate(), LocalTime.of(10, 0), LocalTime.of(10, 30), true, 0);
        when(slotRepo.findByDoctorIdAndSedeIdAndDateAndTime(any(), any(), any(), any()))
                .thenReturn(Optional.of(slot));
        when(slotRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(appointmentRepo.save(any())).thenAnswer(inv -> {
            Appointment a = inv.getArgument(0);
            return new Appointment(42L, a.getDoctorId(), a.getPatientId(), a.getSedeId(),
                    a.getSlotId(), a.getDateTime(), a.getStatus(), null, false, 0);
        });

        Appointment result = service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, VALID_MONDAY_10AM);
        assertNotNull(result);
    }

    @Test
    void book_slotNotAvailable_shouldThrow() {
        when(sedeRepo.existsById(SEDE_ID)).thenReturn(true);
        when(doctorRepo.existsById(DOCTOR_ID)).thenReturn(true);
        when(doctorSedeRepo.existsByDoctorIdAndSedeId(DOCTOR_ID, SEDE_ID)).thenReturn(true);
        when(patientRepo.existsById(PATIENT_ID)).thenReturn(true);
        when(penaltyRepo.countByPatientIdAndDateAfter(eq(PATIENT_ID), any())).thenReturn(0L);

        InventorySlot reservedSlot = new InventorySlot(10L, DOCTOR_ID, SEDE_ID,
                VALID_MONDAY_10AM.toLocalDate(), LocalTime.of(10, 0), LocalTime.of(10, 30), false, 0);
        when(slotRepo.findByDoctorIdAndSedeIdAndDateAndTime(any(), any(), any(), any()))
                .thenReturn(Optional.of(reservedSlot));

        assertThrows(SlotNotAvailableException.class,
                () -> service.book(PATIENT_ID, DOCTOR_ID, SEDE_ID, VALID_MONDAY_10AM));
    }

    @Test
    void cancel_validAppointment_shouldChangeStatus() {
        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(5);
        Appointment existing = new Appointment(1L, DOCTOR_ID, PATIENT_ID, SEDE_ID, 10L,
                futureDateTime, AppointmentStatus.PROGRAMADA, null, false, 0);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(appointmentRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(slotRepo.findByDoctorIdAndSedeIdAndDateAndTime(any(), any(), any(), any()))
                .thenReturn(Optional.empty());

        Appointment result = service.cancel(1L);

        assertEquals(AppointmentStatus.CANCELADA, result.getStatus());
        assertFalse(result.isPenaltyApplied());
    }

    @Test
    void cancel_lessThan2Hours_shouldApplyPenalty() {
        LocalDateTime soonDateTime = LocalDateTime.now().plusMinutes(30);
        Appointment existing = new Appointment(1L, DOCTOR_ID, PATIENT_ID, SEDE_ID, 10L,
                soonDateTime, AppointmentStatus.PROGRAMADA, null, false, 0);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(appointmentRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(slotRepo.findByDoctorIdAndSedeIdAndDateAndTime(any(), any(), any(), any()))
                .thenReturn(Optional.empty());
        when(penaltyRepo.save(any())).thenAnswer(inv -> {
            var p = inv.getArgument(0);
            return new com.ynova.medisalud.appointments.domain.model.Penalty(1L,
                    PATIENT_ID, 1L, LocalDateTime.now());
        });
        when(penaltyRepo.countByPatientIdAndDateAfter(any(), any())).thenReturn(1L);

        Appointment result = service.cancel(1L);

        assertTrue(result.isPenaltyApplied());
        verify(penaltyRepo).save(any());
    }

    @Test
    void cancel_alreadyCancelled_shouldThrow() {
        Appointment cancelled = new Appointment(1L, DOCTOR_ID, PATIENT_ID, SEDE_ID, 10L,
                VALID_MONDAY_10AM, AppointmentStatus.CANCELADA, LocalDateTime.now(), false, 0);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(cancelled));

        assertThrows(InvalidAppointmentStateException.class, () -> service.cancel(1L));
    }

    @Test
    void cancel_notFound_shouldThrow() {
        when(appointmentRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.cancel(99L));
    }
}
