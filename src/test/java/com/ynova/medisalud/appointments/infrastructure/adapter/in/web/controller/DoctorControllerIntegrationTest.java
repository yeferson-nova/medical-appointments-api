package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DoctorControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createDoctor_validRequest_shouldReturn201() throws Exception {
        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-ID", "test-doc-001")
                        .content("""
                                {"name": "Dr. Integration Test", "specialty": "Neurologia", "phone": "5559999", "email": "neuro@test.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dr. Integration Test"))
                .andExpect(jsonPath("$.specialty").value("Neurologia"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void createDoctor_invalidName_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "", "specialty": "Neurologia"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void createDoctor_missingSpecialty_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Dr. Test"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDoctor_seeded_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dra. Maria Gonzalez"))
                .andExpect(jsonPath("$.specialty").value("Cardiologia"));
    }

    @Test
    void getDoctor_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void getAllDoctors_shouldReturnSeededData() throws Exception {
        mockMvc.perform(get("/api/v1/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(3)));
    }

    @Test
    void updateDoctor_existing_shouldReturn200() throws Exception {
        mockMvc.perform(put("/api/v1/doctors/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Dr. Carlos Ruiz Actualizado", "specialty": "Pediatria Neonatal", "phone": "5551002", "email": "carlos@test.com"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr. Carlos Ruiz Actualizado"))
                .andExpect(jsonPath("$.specialty").value("Pediatria Neonatal"));
    }

    @Test
    void deleteDoctor_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/v1/doctors/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createDoctor_responseIncludesLocationHeader() throws Exception {
        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Dr. Location Header", "specialty": "Traumatologia"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}
