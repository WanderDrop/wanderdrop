import { Injectable } from '@angular/core';
import { Attraction } from './attraction.model';

@Injectable({
  providedIn: 'root',
})
export class AttractionService {
  private currentAttraction!: Attraction;
  private currentAttractionId!: number;
  attractions: Attraction[] = [];

  constructor() {}

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
    this.currentAttractionId = attractionId;
    const attraction = this.getAttractionById(attractionId);
    if (attraction) {
      this.setCurrentAttraction(attraction);
    } else {
      console.error('Attraction with id ' + attractionId + ' not found');
    }
  }

  getCurrentAttractionId(): number {
    return this.currentAttractionId;
  }
}
