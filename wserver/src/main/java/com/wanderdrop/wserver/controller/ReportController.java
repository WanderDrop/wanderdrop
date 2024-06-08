package com.wanderdrop.wserver.controller;

import com.wanderdrop.wserver.dto.ReportDto;
import com.wanderdrop.wserver.service.report.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports/attractions/{attractionId}")
    public List<ReportDto> getActiveReportsByAttractionId(@PathVariable Long attractionId) {
        return reportService.getAllActiveReports(attractionId);
    }

    @GetMapping("/reports/active")
    public List<ReportDto> getReportsByAttractionId() {
        return reportService.getAllActiveReports();
    }

    @GetMapping("/reports/closed")
    public List<ReportDto> getAllClosedReports() {
        return reportService.getAllClosedReports();
    }

    @GetMapping("/reports/{id}")
    public ReportDto getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @PostMapping("/reportes/{attractionId}")
    public ReportDto createReport(@RequestBody ReportDto reportDto,@PathVariable Long attractionId ) {
        return reportService.createReport(reportDto, attractionId);
    }

    @PutMapping("/reports/{id}")
    public void deleteReport (@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}