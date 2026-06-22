package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ynova.medisalud.appointments.domain.model.Doctor;
import com.ynova.medisalud.appointments.domain.service.DoctorService;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.CommonHeader;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request.CreateDoctorRequest;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response.DoctorResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> create(CommonHeader headers,
                                                 @Valid @RequestBody CreateDoctorRequest request) {
        Doctor doctor = new Doctor(null, request.name(), request.specialty(),
                request.phone(), request.email());
        Doctor created = doctorService.create(doctor);
        return ResponseEntity.created(URI.create("/api/v1/doctors/" + created.getId()))
                .body(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAll(CommonHeader headers) {
        return ResponseEntity.ok(doctorService.getAll().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getById(CommonHeader headers, @PathVariable Long id) {
        return ResponseEntity.ok(toResponse(doctorService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> update(CommonHeader headers, @PathVariable Long id,
                                                 @Valid @RequestBody CreateDoctorRequest request) {
        Doctor doctor = new Doctor(id, request.name(), request.specialty(),
                request.phone(), request.email());
        return ResponseEntity.ok(toResponse(doctorService.update(id, doctor)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(CommonHeader headers, @PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(doctor.getId(), doctor.getName(), doctor.getSpecialty(),
                doctor.getPhone(), doctor.getEmail());
    }
}
