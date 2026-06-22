package com.ynova.medisalud.appointments.infrastructure.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.InventorySlot;
import com.ynova.medisalud.appointments.domain.model.TimeSlot;
import com.ynova.medisalud.appointments.domain.port.out.DoctorSedeRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.InventorySlotRepositoryPort;
import com.ynova.medisalud.appointments.domain.port.out.SedeRepositoryPort;
import com.ynova.medisalud.appointments.domain.service.BusinessHoursValidator;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.entity.DoctorSedeEntity;
import com.ynova.medisalud.appointments.infrastructure.adapter.out.persistence.repository.JpaDoctorSedeRepository;

@Component
public class InventorySlotGenerator {

    private static final Logger log = LoggerFactory.getLogger(InventorySlotGenerator.class);
    private static final int DAYS_AHEAD = 30;

    private final JpaDoctorSedeRepository doctorSedeRepository;
    private final InventorySlotRepositoryPort inventorySlotRepository;

    public InventorySlotGenerator(JpaDoctorSedeRepository doctorSedeRepository,
                                  InventorySlotRepositoryPort inventorySlotRepository) {
        this.doctorSedeRepository = doctorSedeRepository;
        this.inventorySlotRepository = inventorySlotRepository;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void generateSlotsForNext30Days() {
        log.info("Starting inventory slot generation for next {} days", DAYS_AHEAD);

        List<DoctorSedeEntity> assignments = doctorSedeRepository.findAll();
        LocalDate today = LocalDate.now();
        int totalGenerated = 0;

        for (DoctorSedeEntity ds : assignments) {
            for (int day = 0; day < DAYS_AHEAD; day++) {
                LocalDate date = today.plusDays(day);

                if (inventorySlotRepository.existsByDoctorIdAndSedeIdAndSlotDate(
                        ds.getDoctorId(), ds.getSedeId(), date)) {
                    continue;
                }

                List<TimeSlot> timeSlots = BusinessHoursValidator.generateSlots(date);
                List<InventorySlot> slots = new ArrayList<>();

                for (TimeSlot ts : timeSlots) {
                    LocalTime startTime = ts.start().toLocalTime();
                    LocalTime endTime = ts.end().toLocalTime();
                    slots.add(new InventorySlot(null, ds.getDoctorId(), ds.getSedeId(),
                            date, startTime, endTime, true, 0));
                }

                if (!slots.isEmpty()) {
                    inventorySlotRepository.saveAll(slots);
                    totalGenerated += slots.size();
                }
            }
        }

        log.info("Inventory slot generation complete. Total slots created: {}", totalGenerated);
    }
}
