import { Component, NgZone } from '@angular/core';
import { AttractionService } from '../attraction.service';
import { Router } from '@angular/router';
import { Attraction } from '../attraction.model';
import { FormsModule } from '@angular/forms';
import { MapService } from '../../google-maps/map.service';
import { UserService } from '../../user/user.service';

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
    private ngZone: NgZone,
    private userService: UserService
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
    const userId = this.userService.getDummyUser().UserId;

    const newAttraction = new Attraction(
      this.attractionName,
      this.description,
      this.latitude,
      this.longitude,
      userId
    );

    this.attractionService.addAttraction(newAttraction).subscribe(
      (response) => {
        this.mapService.addMarker(
          response.latitude,
          response.longitude,
          response.id
        );
        this.ngZone.run(() => {
          this.router.navigate(['/home']);
        });
        this.mapService.triggerResetSearchForm();
      },
      (error) => {
        console.error('Error adding new attraction:', error);
      }
    );

    const newAttractionId = newAttraction.id;

    this.attractionService.attractions.push(newAttraction);

    this.mapService.addMarker(this.latitude, this.longitude, newAttractionId);
    this.mapService
      .getMap()
      .setCenter({ lat: this.latitude, lng: this.longitude });

    this.mapService.setNewAttractionLocation(this.latitude, this.longitude);
    this.ngZone.run(() => {
      this.router.navigate(['home']);
    });
    this.mapService.triggerResetSearchForm();
  }
}
