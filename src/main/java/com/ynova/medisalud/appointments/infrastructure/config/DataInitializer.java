package com.ynova.medisalud.appointments.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final InventorySlotGenerator slotGenerator;

    public DataInitializer(InventorySlotGenerator slotGenerator) {
        this.slotGenerator = slotGenerator;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Generating initial inventory slots for the next 30 days");
        slotGenerator.generateSlotsForNext30Days();
    }
}
