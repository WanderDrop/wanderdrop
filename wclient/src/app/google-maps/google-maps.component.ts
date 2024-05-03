import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  GoogleMap,
  GoogleMapsModule,
  MapInfoWindow,
} from '@angular/google-maps';
import { Loader } from '@googlemaps/js-api-loader';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-google-maps',
  standalone: true,
  imports: [GoogleMapsModule, CommonModule],
  templateUrl: './google-maps.component.html',
  styleUrl: './google-maps.component.css',
})
export class GoogleMapsComponent implements OnInit {
  markers: any = [];
  marker: any;
  infoContent = '';
  zoom = 12;
  center!: google.maps.LatLngLiteral;

  options: google.maps.MapOptions = {
    mapTypeId: 'hybrid',
    zoomControl: false,
    scrollwheel: false,
    disableDoubleClickZoom: true,
    maxZoom: 15,
    minZoom: 8,
  };

  map!: google.maps.Map;

  loader: any = new Loader({
    apiKey: environment.API_KEY,
    version: 'weekly',
  });

  ngOnInit() {
    navigator.geolocation.getCurrentPosition((position) => {
      this.center = {
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      };
      this.initMap();
    });
  }

  initMap(): void {
    this.loader
      .load()
      .then(() => {
        this.map = new google.maps.Map(
          document.getElementById('map') as HTMLElement,
          {
            center: this.center,
            zoom: 12,
            mapId: environment.MAP_ID,
            zoomControlOptions: {
              position: google.maps.ControlPosition.RIGHT_CENTER,
            },
          }
        );
        this.map.addListener('click', (event: any) => {
          this.click(event);
        });
      })
      .catch((error: any) => {
        console.error('Error loading Google Maps JavaScript API: ', error);
      });
  }

  async initMarkers(position: any): Promise<void> {
    if (!this.marker) {
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
        })
        .catch((error: any) => {
          console.error('Error loading Google Maps JavaScript API: ', error);
        });
    } else {
      this.marker.position = position;
    }
  }

  click(event: google.maps.MapMouseEvent) {
    console.log(event);
    const position = {
      lat: event.latLng?.lat(),
      lng: event.latLng?.lng(),
    };
    this.initMarkers(position);
  }
}
