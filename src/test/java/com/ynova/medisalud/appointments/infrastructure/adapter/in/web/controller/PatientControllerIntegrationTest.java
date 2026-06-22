package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PatientControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPatient_validRequest_shouldReturn201() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Maria Test", "documentId": "9999999001", "phone": "3101234567", "email": "maria.test@mail.com", "birthDate": "1995-03-20"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Maria Test"))
                .andExpect(jsonPath("$.documentId").value("9999999001"))
                .andExpect(jsonPath("$.birthDate").value("1995-03-20"));
    }

    @Test
    void createPatient_duplicateDocument_shouldReturn409() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Duplicado Test", "documentId": "1234567890", "phone": "3109999999", "email": "dup@mail.com"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_DOCUMENT"));
    }

    @Test
    void createPatient_invalidEmail_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Test Paciente", "documentId": "8888888001", "phone": "3101234567", "email": "no-es-email"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatient_shortDocument_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Test Paciente", "documentId": "123", "phone": "3101234567", "email": "test@mail.com"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatient_futureBirthDate_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Test Paciente", "documentId": "7777777001", "phone": "3101234567", "email": "test@mail.com", "birthDate": "2030-01-01"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPatient_seeded_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Perez"))
                .andExpect(jsonPath("$.documentId").value("1234567890"));
    }

    @Test
    void getPatient_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/patients/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void getAllPatients_shouldReturnSeededData() throws Exception {
        mockMvc.perform(get("/api/v1/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(3)));
    }

    @Test
    void createPatient_missingRequiredFields_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Solo Nombre"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }
}
