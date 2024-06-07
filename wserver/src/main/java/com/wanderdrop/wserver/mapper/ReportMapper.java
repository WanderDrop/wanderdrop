package com.wanderdrop.wserver.mapper;

import com.wanderdrop.wserver.dto.ReportDto;
import com.wanderdrop.wserver.model.Report;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import com.wanderdrop.wserver.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public ReportDto mapToReportDto(Report report) {
        return new ReportDto(
                report.getReportId(),
                report.getReportHeading(),
                report.getReportMessage(),
                report.getAttraction() != null ? report.getAttraction().getAttractionId() : null,
                report.getCreatedBy() != null ? report.getCreatedBy().getUserId() : null,
                report.getCreatedAt(),
                report.getStatus()
        );
    }

    public Report mapToReport(ReportDto reportDto, UserRepository userRepository) {
        Report report = new Report();
        report.setReportId(reportDto.getReportId());
        report.setReportHeading(reportDto.getReportHeading());
        report.setReportMessage(reportDto.getReportMessage());
        report.setStatus(Status.ACTIVE);
        if (reportDto.getCreatedBy() != null) {
            User createdBy = userRepository.findById((reportDto.getCreatedBy())).orElse(null);
            report.setCreatedBy(createdBy);
        }
        report.setCreatedAt(reportDto.getCreatedAt());

        return report;
    }
}
