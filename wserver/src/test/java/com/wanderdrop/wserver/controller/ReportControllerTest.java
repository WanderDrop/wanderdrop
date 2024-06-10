package com.wanderdrop.wserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanderdrop.wserver.dto.ReportDto;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.AttractionRepository;
import com.wanderdrop.wserver.repository.ReportRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import com.wanderdrop.wserver.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
public class ReportControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private User user;
    private User admin;
    private Attraction attraction;
    private String userJwtToken;
    private String adminJwtToken;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("user@reporttest.com");
        user.setPassword("password");
        user.setFirstName("User");
        user.setLastName("Test");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        admin = new User();
        admin.setEmail("admin@reporttest.com");
        admin.setPassword("password");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(Role.ADMIN);
        admin.setStatus(Status.ACTIVE);
        admin.setCreatedAt(LocalDateTime.now());
        admin = userRepository.save(admin);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        userJwtToken = "Bearer " + jwtUtil.generateToken(userDetails);

        UserDetails adminDetails = userDetailsService.loadUserByUsername(admin.getEmail());
        adminJwtToken = "Bearer " + jwtUtil.generateToken(adminDetails);

        attraction = new Attraction();
        attraction.setName("Test Attraction");
        attraction.setDescription("This is a test description.");
        attraction.setLatitude(40.7128);
        attraction.setLongitude(-74.0060);
        attraction.setCreatedBy(admin);
        attraction.setStatus(Status.ACTIVE);
        attraction.setCreatedAt(LocalDateTime.now());
        attraction = attractionRepository.save(attraction);
    }
    @AfterEach
    public void cleanUp() {
        reportRepository.deleteAll();
        attractionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllActiveReports() throws Exception {
        Report report = new Report();
        report.setReportHeading("Test Heading");
        report.setReportMessage("Test Message");
        report.setStatus(Status.ACTIVE);
        report.setAttraction(attraction);
        report.setCreatedBy(admin);
        report.setCreatedAt(LocalDateTime.now());
        report = reportRepository.save(report);

        mockMvc.perform(get("/api/reports/active", attraction.getAttractionId())
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].reportHeading").value("Test Heading"))
                .andExpect(jsonPath("$[0].reportMessage").value("Test Message"));

        reportRepository.delete(report);
    }

    @Test
    public void testGetReportById() throws Exception {
        Report report = new Report();
        report.setReportHeading("Test Heading");
        report.setReportMessage("Test Message");
        report.setStatus(Status.ACTIVE);
        report.setAttraction(attraction);
        report.setCreatedBy(admin);
        report.setCreatedAt(LocalDateTime.now());
        report = reportRepository.save(report);

        mockMvc.perform(get("/api/reports/{id}", report.getReportId())
                        .header("Authorization", userJwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reportHeading").value("Test Heading"))
                .andExpect(jsonPath("$.reportMessage").value("Test Message"));

        reportRepository.delete(report);
    }

    @Test

    public void testCreateReport() throws Exception {
        ReportDto reportDto = new ReportDto();
        reportDto.setReportHeading("Test Attraction");
        reportDto.setReportMessage("New Message");

        mockMvc.perform(post("/api/reportes/{attractionId}", attraction.getAttractionId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportDto))
                        .header("Authorization", userJwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reportHeading").value("Test Attraction"))
                .andExpect(jsonPath("$.reportMessage").value("New Message"));

        List<Report> reports = reportRepository.findAll();
        assertEquals(1, reports.size());
        assertEquals("Test Attraction", reports.get(0).getReportHeading());
        assertEquals("New Message", reports.get(0).getReportMessage());

        reportRepository.deleteAll();
    }
    @Test
    public void testDeleteReport() throws Exception {
        Report report = new Report();
        report.setReportHeading("Test Heading");
        report.setReportMessage("Test Message");
        report.setStatus(Status.ACTIVE);
        report.setAttraction(attraction);
        report.setCreatedBy(admin);
        report.setCreatedAt(LocalDateTime.now());
        report = reportRepository.save(report);


        mockMvc.perform(put("/api/reports/{id}", report.getReportId(), 1L).header("Authorization", adminJwtToken))
                .andExpect(status().isOk());

        Report deletedReport = reportRepository.findById(report.getReportId()).orElse(null);
        assertNotNull(deletedReport);
        assertEquals(Status.DELETED, deletedReport.getStatus());

        reportRepository.delete(deletedReport);
    }

    @Test
    public void testGetAllActiveReports_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/reports/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
