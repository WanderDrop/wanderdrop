package com.wanderdrop.wserver.service.report;

import com.wanderdrop.wserver.dto.ReportDto;
import com.wanderdrop.wserver.model.Status;

import java.util.List;

public interface ReportService {
    ReportDto saveReport(ReportDto reportDto);
    ReportDto getReportById(Long attractionId);
    List<ReportDto> getAllReports();
    void deleteReport(Long reportId);

}