import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Loader } from '@googlemaps/js-api-loader';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class MapService {
  private _position = new BehaviorSubject<{ lat: number; lng: number } | null>(
    null
  );
  private map!: google.maps.Map;

  constructor(private router: Router) {}

  private loader: any = new Loader({
    apiKey: environment.API_KEY,
    version: 'weekly',
    libraries: ['marker'],
  });

  loadGoogleMaps() {
    return this.loader.load();
  }

  setPosition(lat: number, lng: number) {
    this._position.next({ lat, lng });
  }

  getPosition() {
    return this._position.asObservable();
  }

  getMap() {
    return this.map;
  }

  setMap(map: google.maps.Map) {
    console.log('Setting map:', map);
    this.map = map;
  }

  async addMarker(lat: number, lng: number, attractionId: number) {
    console.log('Adding marker at lat:', lat, 'lng:', lng);
    const position = { lat, lng };

    const { AdvancedMarkerElement } = (await google.maps.importLibrary(
      'marker'
    )) as google.maps.MarkerLibrary;

    this.loader
      .load()
      .then(() => {
        const marker = new AdvancedMarkerElement({
          map: this.map,
          position: position,
          gmpDraggable: true,
          zIndex: 2000,
        });
        console.log('Marker:', marker);

        marker.addListener('click', () => {
          // Navigate to the attraction component when the marker is clicked
          this.router.navigate(['/attraction', attractionId]);
        });
      })
      .catch((error: any) => {
        console.error('Error loading Google Maps JavaScript API: ', error);
      });
  }
}
