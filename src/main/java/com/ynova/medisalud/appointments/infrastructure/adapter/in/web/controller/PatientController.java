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

import com.ynova.medisalud.appointments.domain.model.Patient;
import com.ynova.medisalud.appointments.domain.service.PatientService;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.CommonHeader;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request.CreatePatientRequest;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response.PatientResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientResponse> create(CommonHeader headers,
                                                  @Valid @RequestBody CreatePatientRequest request) {
        Patient patient = new Patient(null, request.name(), request.documentId(),
                request.phone(), request.email(), request.birthDate());
        Patient created = patientService.create(patient);
        return ResponseEntity.created(URI.create("/api/v1/patients/" + created.getId()))
                .body(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAll(CommonHeader headers) {
        return ResponseEntity.ok(patientService.getAll().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(CommonHeader headers, @PathVariable Long id) {
        return ResponseEntity.ok(toResponse(patientService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> update(CommonHeader headers, @PathVariable Long id,
                                                  @Valid @RequestBody CreatePatientRequest request) {
        Patient patient = new Patient(id, request.name(), request.documentId(),
                request.phone(), request.email(), request.birthDate());
        return ResponseEntity.ok(toResponse(patientService.update(id, patient)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(CommonHeader headers, @PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PatientResponse toResponse(Patient patient) {
        return new PatientResponse(patient.getId(), patient.getName(), patient.getDocumentId(),
                patient.getPhone(), patient.getEmail(), patient.getBirthDate());
    }
}
