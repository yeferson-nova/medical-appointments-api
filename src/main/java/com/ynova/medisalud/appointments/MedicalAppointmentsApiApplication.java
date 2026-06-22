package com.ynova.medisalud.appointments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedicalAppointmentsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalAppointmentsApiApplication.class, args);
    }
}
