import { Injectable } from '@angular/core';
import { Attraction } from './attraction.model';
import { BehaviorSubject } from 'rxjs';
import { MarkerService } from '../google-maps/marker.service';

@Injectable({
  providedIn: 'root',
})
export class AttractionService {
  private currentAttraction!: Attraction;
  private currentAttractionId = new BehaviorSubject<number | null>(null);
  attractions: Attraction[] = [];

  constructor(private markerService: MarkerService) {}

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
