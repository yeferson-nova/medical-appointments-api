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
import com.ynova.medisalud.appointments.domain.model.DoctorSede;
import com.ynova.medisalud.appointments.domain.model.Sede;
import com.ynova.medisalud.appointments.domain.service.SedeService;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.CommonHeader;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.request.CreateSedeRequest;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response.DoctorResponse;
import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.response.SedeResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sedes")
public class SedeController {

    private final SedeService sedeService;

    public SedeController(SedeService sedeService) {
        this.sedeService = sedeService;
    }

    @PostMapping
    public ResponseEntity<SedeResponse> create(CommonHeader headers,
                                               @Valid @RequestBody CreateSedeRequest request) {
        Sede sede = new Sede(null, request.name(), request.address(), request.city(), request.phone());
        Sede created = sedeService.create(sede);
        return ResponseEntity.created(URI.create("/api/v1/sedes/" + created.getId()))
                .body(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<SedeResponse>> getAll(CommonHeader headers) {
        return ResponseEntity.ok(sedeService.getAll().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeResponse> getById(CommonHeader headers, @PathVariable Long id) {
        return ResponseEntity.ok(toResponse(sedeService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SedeResponse> update(CommonHeader headers, @PathVariable Long id,
                                               @Valid @RequestBody CreateSedeRequest request) {
        Sede sede = new Sede(id, request.name(), request.address(), request.city(), request.phone());
        return ResponseEntity.ok(toResponse(sedeService.update(id, sede)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(CommonHeader headers, @PathVariable Long id) {
        sedeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctors(CommonHeader headers, @PathVariable Long id) {
        List<DoctorResponse> doctors = sedeService.getDoctorsBySede(id).stream()
                .map(this::toDoctorResponse).toList();
        return ResponseEntity.ok(doctors);
    }

    @PostMapping("/{sedeId}/doctors/{doctorId}")
    public ResponseEntity<Void> assignDoctor(CommonHeader headers,
                                             @PathVariable Long sedeId, @PathVariable Long doctorId) {
        sedeService.assignDoctor(sedeId, doctorId);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{sedeId}/doctors/{doctorId}")
    public ResponseEntity<Void> unassignDoctor(CommonHeader headers,
                                               @PathVariable Long sedeId, @PathVariable Long doctorId) {
        sedeService.unassignDoctor(sedeId, doctorId);
        return ResponseEntity.noContent().build();
    }

    private SedeResponse toResponse(Sede sede) {
        return new SedeResponse(sede.getId(), sede.getName(), sede.getAddress(),
                sede.getCity(), sede.getPhone());
    }

    private DoctorResponse toDoctorResponse(Doctor doctor) {
        return new DoctorResponse(doctor.getId(), doctor.getName(), doctor.getSpecialty(),
                doctor.getPhone(), doctor.getEmail());
    }
}
