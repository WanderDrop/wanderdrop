package com.wanderdrop.wserver.service.report;

import com.wanderdrop.wserver.dto.ReportDto;

import java.util.List;

public interface ReportService {
    ReportDto createReport(ReportDto reportDto, Long attractionId);
    ReportDto getReportById(Long reportId);
    List<ReportDto> getAllActiveReports(Long attractionId);
    List<ReportDto> getAllActiveReports();
    List<ReportDto> getAllClosedReports(Long attractionId);
    List<ReportDto> getAllClosedReports();
    void deleteReport(Long reportId);

}
