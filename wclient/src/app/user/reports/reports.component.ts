import { Component, OnInit } from '@angular/core';
import { ReportPage } from '../../report-page/report-page.model';
import { ReportPageService } from '../../report-page/report-page.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent implements OnInit {
  activeReports: ReportPage[] = [];
  closedReports: ReportPage[] = [];

  constructor(private reportService: ReportPageService) { }

  ngOnInit(): void {
    this.fetchActiveReports();
    this.fetchClosedReports();
  }

  fetchActiveReports(): void {
    this.reportService.fetchActiveReports().subscribe((reports: ReportPage[]) => {
      this.activeReports = reports;
    });
  }

  fetchClosedReports(): void {
    this.reportService.fetchClosedReports().subscribe((reports: ReportPage[]) => {
      this.closedReports = reports;
    });
  }

  onDeleteReport(reportId: number, status: string) {
    if (status === 'ACTIVE') {
      this.reportService
        .deleteReport(reportId)
        .subscribe(() => {
          console.log(`Report ${reportId} deleted`)
          this.fetchActiveReports();
          this.fetchClosedReports();
        })
    }
  }
}


