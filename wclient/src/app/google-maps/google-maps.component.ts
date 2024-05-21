import { CommonModule } from '@angular/common';
import {
  Component,
  NgZone,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { GoogleMapsModule } from '@angular/google-maps';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';
import { MapService } from './map.service';
import { AttractionService } from '../attraction/attraction.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-google-maps',
  standalone: true,
  imports: [GoogleMapsModule, CommonModule],
  templateUrl: './google-maps.component.html',
  styleUrl: './google-maps.component.css',
})
export class GoogleMapsComponent implements OnInit, OnDestroy {
  markers: any = [];
  marker: any;
  infoContent = '';
  zoom = 12;
  center!: google.maps.LatLngLiteral | null;
  private positionSubscription!: Subscription;
  private mapInitialized = false;

  options: google.maps.MapOptions = {
    mapTypeId: 'hybrid',
    zoomControl: false,
    scrollwheel: false,
    disableDoubleClickZoom: true,
    maxZoom: 15,
    minZoom: 8,
  };

  map!: google.maps.Map;
  infoWindow!: google.maps.InfoWindow;

  constructor(
    private router: Router,
    private mapService: MapService,
    private ngZone: NgZone,
    private attractionService: AttractionService
  ) {}

  ngOnInit() {
    let newAttractionCreated = false;
    this.center = this.mapService.getLastCenter();

    if (!this.center) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.center = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };
        this.initMap();
        this.mapInitialized = true;
      });
    } else {
      this.initMap();
      this.mapInitialized = true;
    }

    this.positionSubscription = this.mapService
      .getPosition()
      .subscribe((position) => {
        if (
          position &&
          this.map &&
          this.mapInitialized &&
          !newAttractionCreated
        ) {
          this.center = position;
          this.map.setCenter(this.center);
          this.map.panTo(this.center);
        }
      });

    this.mapService.getNewAttractionLocation().subscribe((location) => {
      if (location && this.map && this.mapInitialized) {
        newAttractionCreated = true;
        this.center = location;
        this.map.setCenter(this.center);
        this.map.panTo(this.center);
        this.positionSubscription.unsubscribe();
      }
    });
  }

  initMap(): void {
    this.mapService
      .loadGoogleMaps()
      .then(() => {
        this.infoWindow = new google.maps.InfoWindow();
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
        this.mapService.setMap(this.map);
        this.addAttractionMarkers();
      })
      .catch((error: any) => {
        console.error('Error loading Google Maps JavaScript API: ', error);
      });
  }

  addAttractionMarkers() {
    this.attractionService.fetchAttractions().subscribe((ms) => {
      // this.addAttractionMarkers();
      console.log('Got markers', ms);
      ms.forEach((m: any) => {
        this.mapService.addMarker(
          Number(m.latitude),
          Number(m.longitude),
          m.attractionId
        );
      });
    });
  }

  async initMarkers(position: any): Promise<void> {
    if (!this.marker) {
      const { AdvancedMarkerElement } = (await google.maps.importLibrary(
        'marker'
      )) as google.maps.MarkerLibrary;

      const marker = new AdvancedMarkerElement({
        map: this.map,
        position: position,
        gmpDraggable: false,
        zIndex: 2000,
      });
    } else {
      this.marker.position = position;
    }
  }

  click(event: google.maps.MapMouseEvent) {
    if (event.latLng) {
      const position = {
        lat: event.latLng.lat(),
        lng: event.latLng.lng(),
      };
      this.mapService.setPosition(event.latLng.lat(), event.latLng.lng());

      const contentString = `
      <div class="card" style="width: 16rem;">
        <div class="card-body">
          <h5 class="card-title mb-4 text-center">Add new attraction</h5>
          <button id="add-attraction" class="btn btn-primary float-start ml-2">Add new</button>
          <button id="cancel-attraction" class="btn btn-secondary float-end mr-2">Cancel</button>
        </div>
      </div>
    `;

      this.infoWindow.setContent(contentString);
      this.infoWindow.setPosition(position);

      this.infoWindow.open(this.map);

      google.maps.event.addListenerOnce(this.infoWindow, 'domready', () => {
        document
          .getElementById('add-attraction')
          ?.addEventListener('click', () => {
            this.initMarkers(position);
            this.ngZone.run(() => {
              this.router.navigate(['add-new-attraction']);
            });
          });

        document
          .getElementById('cancel-attraction')
          ?.addEventListener('click', () => {
            this.infoWindow.close();
          });
      });
    }
  }

  ngOnDestroy() {
    if (this.positionSubscription) {
      this.positionSubscription.unsubscribe();
    }
  }
}
