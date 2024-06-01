import { Component, OnInit } from '@angular/core';
import { ReportPage } from '../../report-page/report-page.model';
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

  constructor(){}

  ngOnInit(): void {
    this.reports.forEach(() => {
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

