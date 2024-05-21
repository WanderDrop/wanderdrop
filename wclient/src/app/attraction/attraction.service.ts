import { Injectable, signal } from '@angular/core';
import { Attraction } from './attraction.model';
import { BehaviorSubject, catchError, retry, of, Observable, map } from 'rxjs';
import { MarkerService } from '../google-maps/marker.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { StorageService } from '../user/storage/storage.service';

@Injectable({
  providedIn: 'root',
})
export class AttractionService {
  private currentAttraction!: Attraction;
  private currentAttractionId = new BehaviorSubject<number | null>(null);
  attractions: Attraction[] = [];

  constructor(private markerService: MarkerService, private http: HttpClient) {}

  public fetchAttractions(): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/api/attractions`).pipe(
      retry(3),
      catchError((error) => {
        console.error('Error fetching attractions:', error);
        return of([]);
      })
    );
  }

  fetchAttractionById(id: number): Observable<Attraction> {
    return this.http
      .get<Attraction>(`http://localhost:8080/api/attractions/${id}`)
      .pipe(map((response) => Attraction.fromResponse(response)));
  }

  addAttraction(attraction: Attraction): Observable<Attraction> {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http
      .post<Attraction>(
        'http://localhost:8080/api/attractions',
        attraction.toRequestPayload(),
        { headers }
      )
      .pipe(
        map((response) => Attraction.fromResponse(response)),
        catchError((error) => {
          console.error('Error adding attraction:', error);
          throw error;
        })
      );
  }

  setCurrentAttraction(attraction: Attraction) {
    this.currentAttraction = attraction;
  }

  getCurrentAttraction(): Attraction {
    return this.currentAttraction;
  }

  getAttractionId(): number {
    if (this.currentAttraction) {
      return this.currentAttraction.id;
    } else {
      console.error('Attraction is undefined');
      return -1;
    }
  }

  getLatitude(): number {
    return this.currentAttraction.latitude;
  }

  getLongitude(): number {
    return this.currentAttraction.longitude;
  }

  getAttractionById(id: number): Attraction | undefined {
    return this.attractions.find((attraction) => attraction.id === id);
  }

  setCurrentAttractionId(attractionId: number) {
    this.currentAttractionId.next(attractionId);
    const attraction = this.getAttractionById(attractionId);
    if (attraction) {
      this.setCurrentAttraction(attraction);
    } else {
      console.error('Attraction with id ' + attractionId + ' not found');
    }
  }

  getCurrentAttractionId(): number {
    const attractionId = this.currentAttractionId.getValue();
    if (attractionId !== null) {
      return attractionId;
    } else {
      console.error('Attraction ID is undefined');
      return -1;
    }
  }

  getAttractionIdObservable(): BehaviorSubject<number | null> {
    return this.currentAttractionId;
  }

  deleteAttraction(attractionId: number) {
    this.attractions = this.attractions.filter(
      (attraction) => attraction.id !== attractionId
    );
    this.markerService.removeMarker(attractionId);
  }
}
