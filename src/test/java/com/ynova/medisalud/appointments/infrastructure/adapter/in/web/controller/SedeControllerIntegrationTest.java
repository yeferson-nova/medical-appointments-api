package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SedeControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createSede_validRequest_shouldReturn201() throws Exception {
        mockMvc.perform(post("/api/v1/sedes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Sede Test Integration", "address": "Calle Test 123", "city": "Cali", "phone": "6024567890"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sede Test Integration"))
                .andExpect(jsonPath("$.city").value("Cali"))
                .andExpect(header().exists("Location"));
    }

    @Test
    void createSede_missingName_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/sedes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"address": "Calle 1", "city": "Bogota"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllSedes_shouldReturnSeededData() throws Exception {
        mockMvc.perform(get("/api/v1/sedes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(3)));
    }

    @Test
    void getSede_seeded_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/sedes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MediSalud Sede Norte"));
    }

    @Test
    void getSede_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/sedes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void getDoctorsBySede_seeded_shouldReturnDoctors() throws Exception {
        mockMvc.perform(get("/api/v1/sedes/1/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(2)));
    }

    @Test
    void getDoctorsBySede_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/sedes/999/doctors"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSede_existing_shouldReturn200() throws Exception {
        mockMvc.perform(put("/api/v1/sedes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "MediSalud Sede Norte Actualizada", "address": "Calle 100 #15-20", "city": "Bogota", "phone": "6011234567"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MediSalud Sede Norte Actualizada"));
    }

    @Test
    void deleteSede_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/v1/sedes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void errorResponse_containsRequestId() throws Exception {
        mockMvc.perform(get("/api/v1/sedes/999")
                        .header("X-Request-ID", "error-test-001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.requestId").value("error-test-001"))
                .andExpect(jsonPath("$.path").value("/api/v1/sedes/999"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
}
