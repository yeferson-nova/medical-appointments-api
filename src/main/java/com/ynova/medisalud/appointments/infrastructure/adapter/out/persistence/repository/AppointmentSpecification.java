package com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.domain.Specification;

import com.ynova.medisalud.appointments.domain.model.AppointmentFilter;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.AppointmentEntity;

public final class AppointmentSpecification {

    private AppointmentSpecification() {}

    public static Specification<AppointmentEntity> fromFilter(AppointmentFilter filter) {
        Specification<AppointmentEntity> spec = Specification.where(null);

        if (filter.sedeId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("sedeId"), filter.sedeId()));
        }
        if (filter.doctorId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("doctorId"), filter.doctorId()));
        }
        if (filter.patientId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("patientId"), filter.patientId()));
        }
        if (filter.status() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filter.status()));
        }
        if (filter.startDate() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("dateTime"), filter.startDate().atStartOfDay()));
        }
        if (filter.endDate() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThan(root.get("dateTime"), filter.endDate().plusDays(1).atStartOfDay()));
        }

        return spec;
    }
}
