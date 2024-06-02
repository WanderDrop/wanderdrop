package com.wanderdrop.wserver.controller;

import com.wanderdrop.wserver.dto.ReportDto;
import com.wanderdrop.wserver.service.report.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @GetMapping("/attraction/{attractionId}/active")
    public List<ReportDto> getAllActiveReports(@PathVariable Long attractionId) {
        return reportService.getAllActiveReports(attractionId);
    }

    @GetMapping("/attraction/{attractionId}/closed")
    public List<ReportDto> getAllClosedReports(@PathVariable Long attractionId) {
        return reportService.getAllClosedReports(attractionId);
    }

    @GetMapping("/{id}")
    public ReportDto getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @PostMapping("/{attractionId}")
    public ReportDto createReport(@RequestBody ReportDto reportDto,@PathVariable Long attractionId ) {
        return reportService.createReport(reportDto, attractionId);
    }

    @PutMapping("/{id}")
    public void deleteReport (@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
