import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Subscription } from 'rxjs';
import { StorageService } from '../user/storage/storage.service';

@Injectable({
  providedIn: 'root',
})
export class DeleteReasonService implements OnDestroy {
  private _reasons = new BehaviorSubject<
    { id: number; reasonMessage: string }[]
  >([]);
  private subscriptions: Subscription[] = [];

  constructor(private http: HttpClient) {
    this.fetchReasons();
  }

  get reasons() {
    return this._reasons.asObservable();
  }

  fetchReasons() {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const sub = this.http
      .get<{ id: number; reasonMessage: string }[]>(
        'http://localhost:8080/api/deletion-reasons',
        { headers }
      )
      .subscribe({
        next: (data: { id: number; reasonMessage: string }[]) => {
          this._reasons.next(data);
        },
        error: (error: any) => {
          if (error.error === 'Malformed JWT token') {
            console.error(
              'Error: The authentication token is malformed. Please log in again.'
            );
          } else {
            console.error('Error:', error);
          }
        },
      });
    this.subscriptions.push(sub);
  }

  forceFetchReasons() {
    this.fetchReasons();
  }

  clearReasons() {
    this._reasons.next([]);
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

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
