package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    private User user;
    private Attraction attraction;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setUpdatedAt(Timestamp.from(Instant.now()));
        userRepository.save(user);

        attraction = new Attraction();
        attraction.setName("Test Attraction");
        attraction.setDescription("Description");
        attraction.setLatitude(21.12345);
        attraction.setLongitude(21.12345);
        attraction.setCreatedBy(user);
        attraction.setStatus(Status.ACTIVE);

        attractionRepository.save(attraction);
    }

    @AfterEach
    public void teardown() {
        userRepository.deleteAll();
        reportRepository.deleteAll();
    }

    @Test
    public void testFindByStatusAndAttraction_AttractionId() {
        Report report = new Report();
        report.setReportHeading("Heading");
        report.setReportMessage("Message");
        report.setStatus(Status.ACTIVE);
        report.setAttraction(attraction);
        report.setCreatedBy(user);
        report.setCreatedAt(Timestamp.from(Instant.now()));
        reportRepository.save(report);

        Report newReport = new Report();
        newReport.setReportHeading("New Heading");
        newReport.setReportMessage("New Message");
        newReport.setStatus(Status.DELETED);
        newReport.setAttraction(attraction);
        newReport.setCreatedBy(user);
        newReport.setCreatedAt(Timestamp.from(Instant.now()));
        reportRepository.save(newReport);

        Report report1= new Report();
        report1.setReportHeading("Heading 1");
        report1.setReportMessage("Message 1");
        report1.setStatus(Status.ACTIVE);
        report1.setAttraction(attraction);
        report1.setCreatedBy(user);
        report1.setCreatedAt(Timestamp.from(Instant.now()));
        reportRepository.save(report1);

        List<Report> activeReports = reportRepository.findByStatusAndAttraction_AttractionId(Status.ACTIVE, attraction.getAttractionId());
        List<Report> closedReports = reportRepository.findByStatusAndAttraction_AttractionId(Status.DELETED, attraction.getAttractionId());

        assertNotNull(activeReports);
        assertEquals(2, activeReports.size());
        assertEquals("Heading", activeReports.get(0).getReportHeading());
        assertEquals("Heading 1", activeReports.get(1).getReportHeading());
        assertEquals(1, closedReports.size());
        assertEquals("New Message", closedReports.get(0).getReportMessage());
    }

    @Test
    public void testFindByStatus(){
        Report report = new Report();
        report.setReportHeading("Heading");
        report.setReportMessage("Message");
        report.setStatus(Status.ACTIVE);
        report.setAttraction(attraction);
        report.setCreatedBy(user);
        report.setCreatedAt(Timestamp.from(Instant.now()));
        reportRepository.save(report);

        Report newReport = new Report();
        newReport.setReportHeading("New Heading");
        newReport.setReportMessage("New Message");
        newReport.setStatus(Status.DELETED);
        newReport.setAttraction(attraction);
        newReport.setCreatedBy(user);
        newReport.setCreatedAt(Timestamp.from(Instant.now()));
        reportRepository.save(newReport);

        Report report1= new Report();
        report1.setReportHeading("Heading 1");
        report1.setReportMessage("Message 1");
        report1.setStatus(Status.ACTIVE);
        report1.setAttraction(attraction);
        report1.setCreatedBy(user);
        report1.setCreatedAt(Timestamp.from(Instant.now()));
        reportRepository.save(report1);

        List<Report> activeReports = reportRepository.findByStatus(Status.ACTIVE);
        List<Report> closedReports = reportRepository.findByStatus(Status.DELETED);

        assertNotNull(activeReports);
        assertEquals(2, activeReports.size());
        assertEquals("Heading", activeReports.get(0).getReportHeading());
        assertEquals("Heading 1", activeReports.get(1).getReportHeading());
        assertEquals(1, closedReports.size());
        assertEquals("New Message", closedReports.get(0).getReportMessage());

    }
}
