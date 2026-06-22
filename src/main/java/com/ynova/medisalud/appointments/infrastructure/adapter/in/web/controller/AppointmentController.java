package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynova.medisalud.appointments.domain.model.Appointment;
import com.ynova.medisalud.appointments.domain.model.AppointmentFilter;
import com.ynova.medisalud.appointments.domain.model.AppointmentStatus;
import com.ynova.medisalud.appointments.domain.service.AppointmentService;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.CommonHeader;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request.BookAppointmentRequest;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request.RescheduleAppointmentRequest;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response.AppointmentResponse;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response.TimeSlotResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> book(CommonHeader headers,
                                                    @Valid @RequestBody BookAppointmentRequest request) {
        Appointment created = appointmentService.book(
                request.patientId(), request.doctorId(), request.sedeId(), request.dateTime());
        return ResponseEntity.created(URI.create("/api/v1/appointments/" + created.getId()))
                .body(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> list(
            CommonHeader headers,
            @RequestParam(required = false) Long sedeId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        AppointmentFilter filter = new AppointmentFilter(sedeId, doctorId, patientId, status, startDate, endDate);
        List<AppointmentResponse> responses = appointmentService.findByFilter(filter).stream()
                .map(this::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getById(CommonHeader headers, @PathVariable Long id) {
        return ResponseEntity.ok(toResponse(appointmentService.getById(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(CommonHeader headers, @PathVariable Long id) {
        return ResponseEntity.ok(toResponse(appointmentService.cancel(id)));
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> reschedule(CommonHeader headers, @PathVariable Long id,
                                                          @Valid @RequestBody RescheduleAppointmentRequest request) {
        return ResponseEntity.ok(toResponse(appointmentService.reschedule(id, request.newDateTime())));
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<TimeSlotResponse>> availableSlots(
            CommonHeader headers,
            @RequestParam Long doctorId,
            @RequestParam Long sedeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<TimeSlotResponse> slots = appointmentService.getAvailableSlots(doctorId, sedeId, startDate, endDate)
                .stream()
                .map(s -> new TimeSlotResponse(s.start(), s.end()))
                .toList();
        return ResponseEntity.ok(slots);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(), appointment.getDoctorId(), appointment.getPatientId(),
                appointment.getSedeId(), appointment.getDateTime(),
                appointment.getStatus().name(), appointment.getCancellationTimestamp(),
                appointment.isPenaltyApplied());
    }
}
