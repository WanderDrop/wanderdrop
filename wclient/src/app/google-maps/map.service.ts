import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Loader } from '@googlemaps/js-api-loader';
import { BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';
import { AttractionService } from '../attraction/attraction.service';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class MapService {
  private _position = new BehaviorSubject<{ lat: number; lng: number } | null>(
    null
  );
  private _newAttractionLocation = new BehaviorSubject<{
    lat: number;
    lng: number;
  } | null>(null);
  private map!: google.maps.Map;
  private _resetSearchForm = new BehaviorSubject<boolean>(false);
  private googleMapsLoaded = false;
  private lastCenter: google.maps.LatLngLiteral | null = null;

  constructor(
    private router: Router,
    private attractionService: AttractionService,
    private http: HttpClient
  ) {}

  setNewAttractionLocation(lat: number, lng: number) {
    this._newAttractionLocation.next({ lat, lng });
  }

  getNewAttractionLocation() {
    return this._newAttractionLocation.asObservable();
  }

  getResetSearchForm() {
    return this._resetSearchForm.asObservable();
  }

  getLastCenter(): google.maps.LatLngLiteral | null {
    return this.lastCenter;
  }

  triggerResetSearchForm() {
    this._resetSearchForm.next(true);
  }

  private loader: any = new Loader({
    apiKey: environment.API_KEY,
    version: 'weekly',
    libraries: ['marker', 'places'],
  });

  loadGoogleMaps(): Promise<void> {
    if (!this.googleMapsLoaded) {
      this.googleMapsLoaded = true;
      return this.loader.load({
        libraries: ['marker', 'places'],
      });
    } else {
      return Promise.resolve();
    }
  }

  setPosition(lat: number, lng: number) {
    this._position.next({ lat, lng });
    this.lastCenter = { lat, lng };
  }

  getPosition() {
    return this._position.asObservable();
  }

  getMap() {
    return this.map;
  }

  setMap(map: google.maps.Map) {
    this.map = map;
  }

  geocodeLocation(location: string) {
    console.log('Geocoding location: ', location);
    return this.http
      .get(
        `https://maps.googleapis.com/maps/api/geocode/json?address=${location}&key=${environment.API_KEY}`
      )
      .pipe(
        tap((response) => {
          console.log('Geocoding response: ', response);
        })
      );
  }

  async addMarker(lat: number, lng: number, attractionId: number) {
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
          gmpDraggable: false,
          zIndex: 2000,
        });

        marker.addListener('click', () => {
          this.attractionService.setCurrentAttractionId(attractionId);
          this.router.navigate(['/attraction', attractionId]);
        });
      })
      .catch((error: any) => {
        console.error('Error loading Google Maps JavaScript API: ', error);
      });
  }
}
