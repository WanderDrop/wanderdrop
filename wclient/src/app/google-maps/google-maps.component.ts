import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  GoogleMap,
  GoogleMapsModule,
  MapInfoWindow,
} from '@angular/google-maps';
import { Loader } from '@googlemaps/js-api-loader';

@Component({
  selector: 'app-google-maps',
  standalone: true,
  imports: [GoogleMapsModule, CommonModule],
  templateUrl: './google-maps.component.html',
  styleUrl: './google-maps.component.css',
})
export class GoogleMapsComponent implements OnInit {
  markers: any = [];
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
    apiKey: '',
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
            zoomControlOptions: {
              position: google.maps.ControlPosition.RIGHT_CENTER,
            },
          }
        );
      })
      .catch((error: any) => {
        console.error('Error loading Google Maps JavaScript API: ', error);
      });
  }
}
