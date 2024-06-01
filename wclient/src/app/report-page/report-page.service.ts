import { Injectable } from '@angular/core';
import { ReportPage } from './report-page.model';
import { BehaviorSubject, Observable, catchError, map, of, retry } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { StorageService } from '../user/storage/storage.service';
import { ReportPageStatus } from './report-page-status.enum';

@Injectable({
  providedIn: 'root'
})
export class ReportPageService {  
  reports: ReportPage[] = [];
   private reportsUpdated = new BehaviorSubject<ReportPage[]>([]);

  
  constructor(private http: HttpClient) {}

  public fetchReports() : Observable<any>{
    return this.http.get<any>(`http://localhost:8080/api/reports`).pipe(
      retry(3),
      catchError((error) => {
        console.error('Error fetching reports:', error);
        return of ([]);
      })
    );
  }

  fetchReportById(id: number): Observable<ReportPage>{
    return this.http.get<ReportPage>(`http://localhost:8080/api/${id}`)
    .pipe(map((response) => ReportPage.fromResponse(response)));
  }

  addReport(report:ReportPage):Observable<ReportPage>{
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http
    .post<ReportPage>(
      'http://localhost:8080/api/reports',
      report.toRequestPayLoad(),
      { headers }
    )
    .pipe(
      map((response) =>ReportPage.fromResponse(response)),
      catchError((error) => {
        console.error('Error adding report:', error);
        throw error;
      })
    );
  }
  deleteReport(reportId: number): Observable<void> {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http
      .put<void>(
        `http://localhost:8080/api/attractions/${reportId}`,
        {},
        { headers }
      )
      .pipe(
        catchError((error) => {
          console.error('Error deleting report:', error);
          throw error;
        })
      );
  }

  getReports(reportId: number) {
    return this.reports[reportId] || [];
  }
}
