import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { GoogleMapsModule } from '@angular/google-maps';

@Component({
  selector: 'app-google-maps',
  standalone: true,
  imports: [GoogleMapsModule],
  templateUrl: './google-maps.component.html',
  styleUrl: './google-maps.component.css',
})
export class GoogleMapsComponent {
  center = { lat: 58.391, lng: 24.495 };
}
