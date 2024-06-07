import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { AttractionService } from '../attraction.service';
import { Router } from '@angular/router';
import { Attraction } from '../attraction.model';
import { FormsModule } from '@angular/forms';
import { MapService } from '../../google-maps/map.service';
import { UserService } from '../../user/user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-add-new-attraction',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-new-attraction.component.html',
  styleUrl: './add-new-attraction.component.css',
})
export class AddNewAttractionComponent implements OnInit, OnDestroy {
  attractionName: string = '';
  description: string = '';
  latitude!: number;
  longitude!: number;
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private attractionService: AttractionService,
    private mapService: MapService,
    private ngZone: NgZone,
    private userService: UserService
  ) {}

  ngOnInit() {
    const positionSub = this.mapService.getPosition().subscribe((position) => {
      if (position) {
        this.latitude = position.lat;
        this.longitude = position.lng;
      }
    });
    this.subscriptions.push(positionSub);
  }

  onClose() {
    this.ngZone.run(() => {
      this.router.navigate(['/home']);
    });
  }

  onAddAttraction() {
    const userId = this.userService.getCurrentUser().userId;

    const newAttraction = new Attraction(
      this.attractionName,
      this.description,
      this.latitude,
      this.longitude,
      userId
    );

    const addAttractionSub = this.attractionService
      .addAttraction(newAttraction)
      .subscribe({
        next: (response) => {
          this.attractionService.attractions.push(response);
          this.mapService.addMarker(
            response.latitude,
            response.longitude,
            response.id
          );
          this.mapService
            .getMap()
            .setCenter({ lat: this.latitude, lng: this.longitude });
          this.mapService.setNewAttractionLocation(
            this.latitude,
            this.longitude
          );
          this.ngZone.run(() => {
            this.router.navigate(['/home']);
          });
          this.mapService.triggerResetSearchForm();
        },
        error: (error) => {
          console.error('Error adding new attraction:', error);
        },
      });

    this.subscriptions.push(addAttractionSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
