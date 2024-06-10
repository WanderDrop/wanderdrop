package com.wanderdrop.wserver.mapper;

import com.wanderdrop.wserver.dto.ReportDto;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportMapperTest {
    @InjectMocks
    private ReportMapper reportMapper;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testMapToReportDto() {
        User user = new User();
        user.setUserId(UUID.randomUUID());

        Attraction attraction = new Attraction();
        attraction.setAttractionId(2L);

        Report report = new Report();
        report.setReportId(3L);
        report.setReportHeading("Heading");
        report.setReportMessage("Message");
        report.setCreatedBy(user);
        report.setCreatedAt(LocalDateTime.now());
        report.setAttraction(attraction);

        ReportDto reportDto = reportMapper.mapToReportDto(report);

        assertEquals(report.getReportId(), reportDto.getReportId());
        assertEquals(report.getReportHeading(), reportDto.getReportHeading());
        assertEquals(report.getReportMessage(), reportDto.getReportMessage());
        assertEquals(report.getCreatedBy().getUserId(), reportDto.getCreatedBy());
        assertEquals(report.getCreatedAt(), reportDto.getCreatedAt());
        assertEquals(report.getAttraction().getAttractionId(), reportDto.getAttractionId());
    }

    @Test
    public void testMapToReport_UserNotFound() {
        UUID user = UUID.randomUUID();

        ReportDto reportDto = new ReportDto();
        reportDto.setReportId(3L);
        reportDto.setReportHeading("Heading");
        reportDto.setReportMessage("Message");
        reportDto.setCreatedBy(user);
        reportDto.setCreatedAt(LocalDateTime.now());
        reportDto.setStatus(Status.ACTIVE);
        reportDto.setAttractionId(4L);

        when(userRepository.findById(user)).thenReturn(Optional.empty());

        Report report = reportMapper.mapToReport(reportDto, userRepository);

        assertNotNull(report);
        assertNull(report.getCreatedBy());
    }
}
