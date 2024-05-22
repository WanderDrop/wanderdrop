import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { StorageService } from '../user/storage/storage.service';

@Injectable({
  providedIn: 'root',
})
export class DeleteReasonService {
  private _reasons = new BehaviorSubject<
    { id: number; reasonMessage: string }[]
  >([]);

  constructor(private http: HttpClient) {
    this.fetchReasons();
  }

  get reasons() {
    return this._reasons.asObservable();
  }

  fetchReasons() {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<{ id: number; reasonMessage: string }[]>(
        'http://localhost:8080/api/deletion-reasons',
        { headers }
      )
      .subscribe({
        next: (data: { id: number; reasonMessage: string }[]) => {
          this._reasons.next(data);
        },
        error: (error: any) => {
          console.error('Error:', error);
        },
      });
  }

  postReason(reason: string) {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.post<{ id: number; reasonMessage: string }>(
      'http://localhost:8080/api/deletion-reasons',
      { reasonMessage: reason },
      { headers }
    );
  }

  saveReasonToDatabase(reason: string) {
    this.postReason(reason);
  }
}
