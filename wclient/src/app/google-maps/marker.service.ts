import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class MarkerService {
  private markers = new Map<number, any>();

  constructor() {}

  addMarker(attractionId: number, marker: any) {
    this.markers.set(attractionId, marker);
  }

  removeMarker(attractionId: number) {
    const marker = this.markers.get(attractionId);
    if (marker) {
      marker.setMap(null); 
      this.markers.delete(attractionId);
    }
  }
}
