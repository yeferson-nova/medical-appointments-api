package com.ynova.medisalud.appointments.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ynova.medisalud.appointments.domain.exception.ResourceNotFoundException;
import com.ynova.medisalud.appointments.domain.model.Doctor;
import com.ynova.medisalud.appointments.domain.model.DoctorSede;
import com.ynova.medisalud.appointments.domain.model.Sede;
import com.ynova.medisalud.appointments.domain.port.out.DoctorRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.DoctorSedeRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.SedeRepositoryPort;

@Service
public class SedeService {

    private final SedeRepositoryPort sedeRepository;
    private final DoctorRepositoryPort doctorRepository;
    private final DoctorSedeRepositoryPort doctorSedeRepository;

    public SedeService(SedeRepositoryPort sedeRepository,
                       DoctorRepositoryPort doctorRepository,
                       DoctorSedeRepositoryPort doctorSedeRepository) {
        this.sedeRepository = sedeRepository;
        this.doctorRepository = doctorRepository;
        this.doctorSedeRepository = doctorSedeRepository;
    }

    public Sede create(Sede sede) {
        return sedeRepository.save(sede);
    }

    public Sede getById(Long id) {
        return sedeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", id));
    }

    public List<Sede> getAll() {
        return sedeRepository.findAll();
    }

    public Sede update(Long id, Sede sede) {
        if (!sedeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sede", id);
        }
        Sede updated = new Sede(id, sede.getName(), sede.getAddress(), sede.getCity(), sede.getPhone());
        return sedeRepository.save(updated);
    }

    public void delete(Long id) {
        if (!sedeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sede", id);
        }
        sedeRepository.deleteById(id);
    }

    public DoctorSede assignDoctor(Long sedeId, Long doctorId) {
        if (!sedeRepository.existsById(sedeId)) {
            throw new ResourceNotFoundException("Sede", sedeId);
        }
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", doctorId);
        }
        DoctorSede doctorSede = new DoctorSede(null, doctorId, sedeId);
        return doctorSedeRepository.save(doctorSede);
    }

    public void unassignDoctor(Long sedeId, Long doctorId) {
        if (!sedeRepository.existsById(sedeId)) {
            throw new ResourceNotFoundException("Sede", sedeId);
        }
        doctorSedeRepository.deleteByDoctorIdAndSedeId(doctorId, sedeId);
    }

    public List<Doctor> getDoctorsBySede(Long sedeId) {
        if (!sedeRepository.existsById(sedeId)) {
            throw new ResourceNotFoundException("Sede", sedeId);
        }
        return doctorSedeRepository.findDoctorsBySedeId(sedeId);
    }
}
