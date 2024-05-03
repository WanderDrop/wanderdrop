import { Component, NgZone } from '@angular/core';
import { AttractionService } from '../attraction.service';
import { Router } from '@angular/router';
import { GoogleMapsComponent } from '../../google-maps/google-maps.component';
import { Attraction } from '../attraction.model';
import { FormsModule } from '@angular/forms';
import { MapService } from '../../google-maps/map.service';

@Component({
  selector: 'app-add-new-attraction',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-new-attraction.component.html',
  styleUrl: './add-new-attraction.component.css',
})
export class AddNewAttractionComponent {
  attractionName: string = '';
  description: string = '';
  latitude!: number;
  longitude!: number;

  constructor(
    private router: Router,
    private attractionService: AttractionService,
    private mapService: MapService,
    private ngZone: NgZone
  ) {}

  ngOnInit() {
    this.mapService.getPosition().subscribe((position) => {
      if (position) {
        this.latitude = position.lat;
        this.longitude = position.lng;
      }
    });
  }

  onClose() {
    this.ngZone.run(() => {
      this.router.navigate(['/home']);
    });
  }

  onAddAttraction() {
    console.log(
      `Adding marker at lat: ${this.latitude}, lng: ${this.longitude}`
    );

    const attraction = new Attraction(
      this.attractionName,
      this.description,
      this.latitude,
      this.longitude,
      'Eleri'
    );

    this.attractionService.attractions.push(attraction);

    this.mapService.addMarker(this.latitude, this.longitude);
    this.mapService
      .getMap()
      .setCenter({ lat: this.latitude, lng: this.longitude });

    console.log(
      this.latitude,
      this.longitude,
      attraction.name,
      attraction.description
    );

    this.ngZone.run(() => {
      this.router.navigate(['home']);
    });
  }
}
