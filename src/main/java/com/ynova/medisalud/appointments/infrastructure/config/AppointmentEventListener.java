package com.ynova.medisalud.appointments.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.event.AppointmentBookedEvent;
import com.ynova.medisalud.appointments.domain.event.AppointmentCancelledEvent;
import com.ynova.medisalud.appointments.domain.event.AppointmentRescheduledEvent;
import com.ynova.medisalud.appointments.domain.event.PenaltyRegisteredEvent;

@Component
public class AppointmentEventListener {

    private static final Logger log = LoggerFactory.getLogger(AppointmentEventListener.class);

    @EventListener
    public void onAppointmentBooked(AppointmentBookedEvent event) {
        log.info("Event: Appointment booked - id={}, doctorId={}, patientId={}, sedeId={}, dateTime={}",
                event.appointmentId(), event.doctorId(), event.patientId(),
                event.sedeId(), event.dateTime());
    }

    @EventListener
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        log.info("Event: Appointment cancelled - id={}, patientId={}, penalty={}",
                event.appointmentId(), event.patientId(), event.penaltyApplied());
    }

    @EventListener
    public void onAppointmentRescheduled(AppointmentRescheduledEvent event) {
        log.info("Event: Appointment rescheduled - oldId={}, newId={}",
                event.oldAppointmentId(), event.newAppointmentId());
    }

    @EventListener
    public void onPenaltyRegistered(PenaltyRegisteredEvent event) {
        log.warn("Event: Penalty registered - patientId={}, penaltyId={}, totalLast30Days={}",
                event.patientId(), event.penaltyId(), event.totalPenaltiesLast30Days());
    }
}
