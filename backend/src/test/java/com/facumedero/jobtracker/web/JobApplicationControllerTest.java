package com.facumedero.jobtracker.web;

import com.facumedero.jobtracker.domain.ApplicationStatus;
import com.facumedero.jobtracker.dto.JobApplicationRequest;
import com.facumedero.jobtracker.dto.JobApplicationResponse;
import com.facumedero.jobtracker.service.JobApplicationService;
import com.facumedero.jobtracker.service.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de la capa web: levanta solo el controller (y el @RestControllerAdvice) con el servicio simulado.
 */
@WebMvcTest(JobApplicationController.class)
class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobApplicationService service;

    private JobApplicationResponse sample() {
        return new JobApplicationResponse(1L, "Acme", "Java Dev", ApplicationStatus.APPLIED,
                "LinkedIn", null, null, LocalDate.of(2026, 11, 1), Instant.parse("2026-11-01T10:00:00Z"));
    }

    @Test
    void list_returnsApplications() throws Exception {
        when(service.findAll(null)).thenReturn(List.of(sample()));

        mockMvc.perform(get("/api/applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].company").value("Acme"))
                .andExpect(jsonPath("$[0].status").value("APPLIED"));
    }

    @Test
    void create_withValidBody_returns201AndLocation() throws Exception {
        when(service.create(any(JobApplicationRequest.class))).thenReturn(sample());

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"company": "Acme", "title": "Java Dev"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/applications/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_withBlankCompany_returns400WithFieldErrors() throws Exception {
        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"company": "", "title": "Java Dev"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.company").exists());
    }

    @Test
    void get_whenMissing_returns404() throws Exception {
        when(service.findById(42L)).thenThrow(new NotFoundException("No existe la postulación con id 42"));

        mockMvc.perform(get("/api/applications/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No existe la postulación con id 42"));
    }
}
