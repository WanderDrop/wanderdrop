import { Injectable } from '@angular/core';
import { ReportPage } from './report-page.model';
import { Observable, catchError, of, retry } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { StorageService } from '../user/storage/storage.service';
import { AttractionComponent } from '../attraction/attraction.component';

@Injectable({
  providedIn: 'root'
})
export class ReportPageService {
  reports: ReportPage[] = [];
  private baseUrl = `http://localhost:8080/api`;


  constructor(private http: HttpClient) {}

  public fetchReports() : Observable<any>{
    return this.http.get<any>( `${this.baseUrl}/reportes`).pipe(
      retry(3),
      catchError((error) => {
        console.error('Error fetching reports:', error);
        return of ([]);
      })
    );
  }

  addReport(
    attractionId: number,
    reportData:  {reportHeading:  AttractionComponent["attractionName"]; reportMessage: string}):
  Observable<ReportPage>{
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http
    .post<ReportPage>(
      `${this.baseUrl}/reportes/${attractionId}`,
      reportData,
      { headers }
    )
  }
  deleteReport(reportId: number): Observable<void> {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http
      .put<void>(
        `${this.baseUrl}/reportes/${reportId}`,
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

  getReports(attractionId: number): Observable<ReportPage[]> {
    return this.http.get<ReportPage[]>( `${this.baseUrl}/reportes/attractions/${attractionId}/active`);
  }
}
