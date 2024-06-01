package com.wanderdrop.wserver.service.report;

import com.wanderdrop.wserver.dto.ReportDto;
import com.wanderdrop.wserver.mapper.ReportMapper;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.AttractionRepository;
import com.wanderdrop.wserver.repository.ReportRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportServiceImpl(UserRepository userRepository, ReportRepository reportRepository, ReportMapper reportMapper) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    @Override
    public ReportDto saveReport (ReportDto reportDto) {

        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null || (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.USER)) {
            throw new AccessDeniedException("Only logged in users and admins can create an attraction.");
        }

        reportDto.setCreatedBy(currentUser.getUserId());
        Report report = reportMapper.mapToReport(reportDto, userRepository);
        report.setCreatedBy(currentUser);
        report.setStatus(Status.ACTIVE);
        report.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Report savedReport = reportRepository.save(report);
        return reportMapper.mapToReportDto(savedReport);
    }

    @Override
    public ReportDto getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);
        return report != null ? reportMapper.mapToReportDto(report) : null;
    }

    @Override
    public List<ReportDto> getAllReports() {
        return reportRepository.findAll().stream()
                .filter(report -> report.getStatus() == Status.ACTIVE)
                .map(reportMapper::mapToReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReport(Long reportId) {

        Optional<Report> optionalReport = reportRepository.findById(reportId);
        if (optionalReport.isPresent() && optionalReport.get().getStatus() == Status.ACTIVE) {
            Report report = optionalReport.get();
            report.setStatus(Status.DELETED);

            reportRepository.save(report);
        } else {
            throw new IllegalArgumentException("Report with id " + reportId + " not found or Report is already deleted");
        }
    }

    private User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElse(null);
    }

}
