package com.abdimaalik.clinic;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abdimaalik.clinic.repository.AppointmentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerIntegrationTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        appointmentRepository.deleteAll();
    }

    private String createAppointmentAndReturnId() throws Exception {
        String requestBody = """
                {
                  "patientName": "Ali Hassan",
                  "clinicianName": "Dr Brown",
                  "startTime": "2026-03-20T10:00:00",
                  "endTime": "2026-03-20T10:30:00",
                  "fee": 75.00
                }
                """;

        String response = mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientName").value("Ali Hassan"))
                .andExpect(jsonPath("$.clinicianName").value("Dr Brown"))
                .andExpect(jsonPath("$.fee").value(new BigDecimal("75.00").doubleValue()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("id").asText();
    }

    @Test
    void shouldScheduleAppointmentSuccessfully() throws Exception {
        createAppointmentAndReturnId();
    }

    @Test
    void shouldRejectOverlappingAppointment() throws Exception {
        createAppointmentAndReturnId();

        String overlappingRequestBody = """
                {
                  "patientName": "Sara Khan",
                  "clinicianName": "Dr Brown",
                  "startTime": "2026-03-20T10:15:00",
                  "endTime": "2026-03-20T10:45:00",
                  "fee": 80.00
                }
                """;

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(overlappingRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Appointment overlaps with an existing active appointment for this clinician."));
    }

    @Test
    void shouldCancelAppointmentSuccessfully() throws Exception {
        String appointmentId = createAppointmentAndReturnId();

        mockMvc.perform(patch("/appointments/" + appointmentId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldAllowRebookingAfterCancellation() throws Exception {
        String appointmentId = createAppointmentAndReturnId();

        mockMvc.perform(patch("/appointments/" + appointmentId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        String rebookingRequestBody = """
                {
                  "patientName": "Sara Khan",
                  "clinicianName": "Dr Brown",
                  "startTime": "2026-03-20T10:15:00",
                  "endTime": "2026-03-20T10:45:00",
                  "fee": 80.00
                }
                """;

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rebookingRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldReturnPaginatedAppointments() throws Exception {
        createAppointmentAndReturnId();

        mockMvc.perform(get("/appointments?page=0&size=5&sortBy=startTime&direction=asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(0));
    }
}