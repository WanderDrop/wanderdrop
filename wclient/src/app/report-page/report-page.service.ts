import { Injectable } from '@angular/core';
import { ReportPage } from './report-page.model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportPageService {  
  reports:{[attractionId: number] : ReportPage[] } = {};
  private reportsUpdated = new BehaviorSubject<ReportPage[]>([]);
  
  constructor() {}

  getReportsUpdated() {
    return this.reportsUpdated.asObservable();
  }

  getReports(attractionId: number) {
    return this.reports[attractionId] || [];
  }

  fetchReportPage(attractionId: number) {
    const reports = this.reports[attractionId] || [];
    this.reportsUpdated.next(reports);
  }

  addReportPage(reportPage: ReportPage) {
    if (!this.reports[reportPage.attractionId]) {
      this.reports[reportPage.attractionId] = [];
    }
    this.reports[reportPage.attractionId].push(reportPage);
    this.reportsUpdated.next(this.reports[reportPage.attractionId]);
  }

  deleteReportPage(reportPageId: number, attractionId: number) {
    if (this.reports[attractionId]) {
      this.reports[attractionId] = this.reports[attractionId].filter(
        (reportPage) => reportPage.reportId !== reportPageId
      );
      this.reportsUpdated.next(this.reports[attractionId]);
    }
  }
}
