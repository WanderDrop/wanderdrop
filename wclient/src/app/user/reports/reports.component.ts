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
export class ReportsComponent implements OnInit{
  reports: ReportPage[] = [];

  constructor(
    private reportPageService: ReportPageService
  ){}

  ngOnInit(): void {
    this.reports.forEach((attraction) => {
      this.reports = [...this.reports];
    });
  }
  get activeReports() {
    return this.reports.filter(reportPage => reportPage.status === 'active');
  }

  get deletedReports() {
    return this.reports.filter(reportPage => reportPage.status === 'deleted')
  }

}

