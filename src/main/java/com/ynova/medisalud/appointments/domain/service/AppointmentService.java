package com.ynova.medisalud.appointments.domain.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynova.medisalud.appointments.domain.event.AppointmentBookedEvent;
import com.ynova.medisalud.appointments.domain.event.AppointmentCancelledEvent;
import com.ynova.medisalud.appointments.domain.event.AppointmentRescheduledEvent;
import com.ynova.medisalud.appointments.domain.event.PenaltyRegisteredEvent;
import com.ynova.medisalud.appointments.domain.exception.DuplicateAppointmentException;
import com.ynova.medisalud.appointments.domain.exception.InvalidScheduleException;
import com.ynova.medisalud.appointments.domain.exception.PatientBlockedException;
import com.ynova.medisalud.appointments.domain.exception.ResourceNotFoundException;
import com.ynova.medisalud.appointments.domain.exception.SlotNotAvailableException;
import com.ynova.medisalud.appointments.domain.model.Appointment;
import com.ynova.medisalud.appointments.domain.model.AppointmentFilter;
import com.ynova.medisalud.appointments.domain.model.InventorySlot;
import com.ynova.medisalud.appointments.domain.model.Penalty;
import com.ynova.medisalud.appointments.domain.model.TimeSlot;
import com.ynova.medisalud.appointments.domain.port.out.AppointmentRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.DoctorRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.DoctorSedeRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.InventorySlotRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.PatientRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.PenaltyRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.SedeRepositoryPort;

@Service
public class AppointmentService {

    private static final long MAX_PENALTIES_30_DAYS = 3;
    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepositoryPort appointmentRepository;
    private final InventorySlotRepositoryPort inventorySlotRepository;
    private final DoctorRepositoryPort doctorRepository;
    private final PatientRepositoryPort patientRepository;
    private final SedeRepositoryPort sedeRepository;
    private final DoctorSedeRepositoryPort doctorSedeRepository;
    private final PenaltyRepositoryPort penaltyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AppointmentService(AppointmentRepositoryPort appointmentRepository,
                              InventorySlotRepositoryPort inventorySlotRepository,
                              DoctorRepositoryPort doctorRepository,
                              PatientRepositoryPort patientRepository,
                              SedeRepositoryPort sedeRepository,
                              DoctorSedeRepositoryPort doctorSedeRepository,
                              PenaltyRepositoryPort penaltyRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.inventorySlotRepository = inventorySlotRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.sedeRepository = sedeRepository;
        this.doctorSedeRepository = doctorSedeRepository;
        this.penaltyRepository = penaltyRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Appointment book(Long patientId, Long doctorId, Long sedeId, LocalDateTime dateTime) {
        LocalDateTime normalized = normalizeToSlot(dateTime);

        log.info("Booking request: doctorId={}, patientId={}, sedeId={}, dateTime={}",
                doctorId, patientId, sedeId, normalized);

        BusinessHoursValidator.validate(normalized);

        if (!sedeRepository.existsById(sedeId)) {
            throw new ResourceNotFoundException("Sede", sedeId);
        }
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", doctorId);
        }
        if (!doctorSedeRepository.existsByDoctorIdAndSedeId(doctorId, sedeId)) {
            throw new InvalidScheduleException("El medico " + doctorId + " no atiende en la sede " + sedeId);
        }
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Paciente", patientId);
        }

        long penalties = penaltyRepository.countByPatientIdAndDateAfter(
                patientId, LocalDateTime.now().minusDays(30));
        log.debug("Penalty check: patientId={}, count={}", patientId, penalties);
        if (penalties >= MAX_PENALTIES_30_DAYS) {
            log.warn("Patient blocked: patientId={}, penalties={}", patientId, penalties);
            throw new PatientBlockedException(patientId, penalties);
        }

        InventorySlot slot = inventorySlotRepository.findByDoctorIdAndSedeIdAndDateAndTime(
                        doctorId, sedeId, normalized.toLocalDate(), normalized.toLocalTime())
                .orElseThrow(() -> new SlotNotAvailableException(
                        "No existe franja disponible para el medico " + doctorId
                                + " en la sede " + sedeId + " a las " + normalized));

        if (!slot.isAvailable()) {
            throw new SlotNotAvailableException(doctorId, normalized);
        }

        slot.reserve();
        inventorySlotRepository.save(slot);

        Appointment appointment = Appointment.create(doctorId, patientId, sedeId, slot.getId(), normalized);
        Appointment saved = appointmentRepository.save(appointment);

        log.info("Appointment booked: id={}, doctorId={}, patientId={}", saved.getId(), doctorId, patientId);
        eventPublisher.publishEvent(new AppointmentBookedEvent(
                saved.getId(), doctorId, patientId, sedeId, normalized));

        return saved;
    }

    @Transactional
    public Appointment cancel(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", appointmentId));

        boolean penalized = appointment.cancel(LocalDateTime.now());
        Appointment saved = appointmentRepository.save(appointment);

        if (appointment.getSlotId() != null) {
            inventorySlotRepository.findByDoctorIdAndSedeIdAndDateAndTime(
                    appointment.getDoctorId(), appointment.getSedeId(),
                    appointment.getDateTime().toLocalDate(), appointment.getDateTime().toLocalTime()
            ).ifPresent(slot -> {
                slot.release();
                inventorySlotRepository.save(slot);
            });
        }

        if (penalized) {
            Penalty penalty = Penalty.create(appointment.getPatientId(), appointmentId, LocalDateTime.now());
            Penalty savedPenalty = penaltyRepository.save(penalty);
            long totalPenalties = penaltyRepository.countByPatientIdAndDateAfter(
                    appointment.getPatientId(), LocalDateTime.now().minusDays(30));
            log.warn("Penalty registered: patientId={}, total={}", appointment.getPatientId(), totalPenalties);
            eventPublisher.publishEvent(new PenaltyRegisteredEvent(
                    appointment.getPatientId(), savedPenalty.getId(), totalPenalties));
        }

        log.info("Appointment cancelled: id={}, penalty={}", appointmentId, penalized);
        eventPublisher.publishEvent(new AppointmentCancelledEvent(
                appointmentId, appointment.getPatientId(), penalized));

        return saved;
    }

    @Transactional
    public Appointment reschedule(Long appointmentId, LocalDateTime newDateTime) {
        Appointment oldAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", appointmentId));

        cancel(appointmentId);

        Appointment newAppointment = book(
                oldAppointment.getPatientId(),
                oldAppointment.getDoctorId(),
                oldAppointment.getSedeId(),
                newDateTime);

        log.info("Appointment rescheduled: oldId={}, newId={}", appointmentId, newAppointment.getId());
        eventPublisher.publishEvent(new AppointmentRescheduledEvent(appointmentId, newAppointment.getId()));

        return newAppointment;
    }

    public Appointment getById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));
    }

    public List<Appointment> findByFilter(AppointmentFilter filter) {
        return appointmentRepository.findByFilter(filter);
    }

    public List<TimeSlot> getAvailableSlots(Long doctorId, Long sedeId,
                                            java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", doctorId);
        }
        if (!sedeRepository.existsById(sedeId)) {
            throw new ResourceNotFoundException("Sede", sedeId);
        }

        List<InventorySlot> available = inventorySlotRepository
                .findAvailableByDoctorAndSedeAndDateRange(doctorId, sedeId, startDate, endDate);

        return available.stream()
                .map(s -> new TimeSlot(
                        s.getSlotDate().atTime(s.getTimeStart()),
                        s.getSlotDate().atTime(s.getTimeEnd())))
                .toList();
    }

    private LocalDateTime normalizeToSlot(LocalDateTime dateTime) {
        int minutes = dateTime.getMinute();
        int normalized = (minutes < 30) ? 0 : 30;
        return dateTime.withMinute(normalized).withSecond(0).withNano(0);
    }
}
