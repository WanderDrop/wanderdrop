import { Injectable } from '@angular/core';
import { ReportPage } from './report-page.model';
import { Observable, catchError, map, of, retry } from 'rxjs';
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
  public fetchActiveReports(): Observable<ReportPage[]> {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http
      .get<ReportPage[]>(`http://localhost:8080/api/reports/active`, {
        headers,
      })
      .pipe(
        retry(3),
        catchError((error) => {
          console.error('Error fetching user attractions:', error);
          return of([]);
        })
      );
  }
  public fetchClosedReports(): Observable<ReportPage[]> {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http
      .get<ReportPage[]>(`http://localhost:8080/api/reports/closed`, {
        headers,
      })
      .pipe(
        retry(3),
        catchError((error) => {
          console.error('Error fetching user attractions:', error);
          return of([]);
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
        `${this.baseUrl}/reports/${reportId}`,
        {
          status: "DELETED"
        },
        { headers }
      );
  }


  getReports(attractionId: number): Observable<ReportPage[]> {
    return this.http.get<ReportPage[]>( `${this.baseUrl}/reportes/attractions/${attractionId}/active`);
  }
}
