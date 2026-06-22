package com.ynova.medisalud.appointments.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ynova.medisalud.appointments.domain.exception.ResourceNotFoundException;
import com.ynova.medisalud.appointments.domain.model.Doctor;
import com.ynova.medisalud.appointments.domain.port.out.DoctorRepositoryPort;

@Service
public class DoctorService {

    private final DoctorRepositoryPort doctorRepository;

    public DoctorService(DoctorRepositoryPort doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor create(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor getById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
    }

    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    public Doctor update(Long id, Doctor doctor) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor", id);
        }
        Doctor updated = new Doctor(id, doctor.getName(), doctor.getSpecialty(),
                doctor.getPhone(), doctor.getEmail());
        return doctorRepository.save(updated);
    }

    public void delete(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor", id);
        }
        doctorRepository.deleteById(id);
    }
}
