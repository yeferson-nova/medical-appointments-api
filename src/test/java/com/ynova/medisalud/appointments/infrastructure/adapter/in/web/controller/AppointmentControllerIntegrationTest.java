package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AppointmentControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String nextWeekday() {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Test
    void bookAppointment_validRequest_shouldReturn201() throws Exception {
        String date = nextWeekday();
        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-ID", "book-001")
                        .content("""
                                {"patientId": 1, "doctorId": 1, "sedeId": 1, "dateTime": "%sT10:00:00"}
                                """.formatted(date)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PROGRAMADA"))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.sedeId").value(1))
                .andExpect(header().exists("Location"))
                .andExpect(header().exists("X-Request-ID"));
    }

    @Test
    void bookAppointment_sameSlotTwice_shouldReturn409() throws Exception {
        String date = nextWeekday();
        String body = """
                {"patientId": 2, "doctorId": 1, "sedeId": 1, "dateTime": "%sT08:30:00"}
                """.formatted(date);

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"patientId": 3, "doctorId": 1, "sedeId": 1, "dateTime": "%sT08:30:00"}
                                """.formatted(date)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("SLOT_NOT_AVAILABLE"));
    }

    @Test
    void bookAppointment_doctorNotInSede_shouldReturn400() throws Exception {
        String date = nextWeekday();
        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"patientId": 1, "doctorId": 3, "sedeId": 3, "dateTime": "%sT09:00:00"}
                                """.formatted(date)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_SCHEDULE"));
    }

    @Test
    void bookAppointment_doctorNotFound_shouldReturn404() throws Exception {
        String date = nextWeekday();
        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"patientId": 1, "doctorId": 999, "sedeId": 1, "dateTime": "%sT09:00:00"}
                                """.formatted(date)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void bookAppointment_patientNotFound_shouldReturn404() throws Exception {
        String date = nextWeekday();
        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"patientId": 999, "doctorId": 1, "sedeId": 1, "dateTime": "%sT09:00:00"}
                                """.formatted(date)))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelAppointment_valid_shouldReturn200() throws Exception {
        String date = nextWeekday();
        MvcResult result = mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"patientId": 3, "doctorId": 2, "sedeId": 1, "dateTime": "%sT11:00:00"}
                                """.formatted(date)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String id = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(responseBody).get("id").asText();

        mockMvc.perform(patch("/api/v1/appointments/" + id + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"))
                .andExpect(jsonPath("$.cancellationTimestamp").isNotEmpty());
    }

    @Test
    void cancelAppointment_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(patch("/api/v1/appointments/999/cancel"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvailableSlots_validParams_shouldReturnSlots() throws Exception {
        String date = nextWeekday();
        mockMvc.perform(get("/api/v1/appointments/available-slots")
                        .param("doctorId", "1")
                        .param("sedeId", "1")
                        .param("startDate", date)
                        .param("endDate", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)))
                .andExpect(jsonPath("$[0].start").isNotEmpty())
                .andExpect(jsonPath("$[0].end").isNotEmpty());
    }

    @Test
    void getAvailableSlots_doctorNotFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/appointments/available-slots")
                        .param("doctorId", "999")
                        .param("sedeId", "1")
                        .param("startDate", "2026-07-06")
                        .param("endDate", "2026-07-06"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listAppointments_withFilters_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/appointments")
                        .param("doctorId", "1")
                        .param("status", "PROGRAMADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void listAppointments_noFilters_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAppointment_existing_shouldReturn200() throws Exception {
        String date = nextWeekday();
        MvcResult result = mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"patientId": 1, "doctorId": 2, "sedeId": 3, "dateTime": "%sT14:00:00"}
                                """.formatted(date)))
                .andExpect(status().isCreated())
                .andReturn();

        String id = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(result.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(get("/api/v1/appointments/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Integer.parseInt(id)));
    }

    @Test
    void getAppointment_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/appointments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void bookAppointment_responseContainsRequestId() throws Exception {
        String date = nextWeekday();
        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-ID", "custom-request-123")
                        .content("""
                                {"patientId": 1, "doctorId": 1, "sedeId": 2, "dateTime": "%sT15:00:00"}
                                """.formatted(date)))
                .andExpect(status().isCreated())
                .andExpect(header().string("X-Request-ID", "custom-request-123"));
    }
}
