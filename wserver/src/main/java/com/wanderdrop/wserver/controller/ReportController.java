package com.wanderdrop.wserver.controller;

import com.wanderdrop.wserver.dto.ReportDto;
import com.wanderdrop.wserver.service.report.ReportServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportServiceImpl reportServiceImpl;

    public ReportController(ReportServiceImpl reportServiceImpl) {
        this.reportServiceImpl = reportServiceImpl;
    }

    @GetMapping
    public List<ReportDto> getAllReports() {
        return reportServiceImpl.getAllReports();
    }

    @GetMapping("/{id}")
    public ReportDto getReportById(@PathVariable Long id) {
        return reportServiceImpl.getReportById(id);
    }

    @PostMapping
    public ReportDto createReport(@RequestBody ReportDto reportDto) {
        return reportServiceImpl.saveReport(reportDto);
    }

    @PutMapping("/{id}")
    public void deleteReport (@PathVariable Long id) {
        reportServiceImpl.deleteReport(id);
    }
}
