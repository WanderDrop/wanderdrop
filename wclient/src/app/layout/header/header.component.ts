import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ProfileDropdownComponent } from './profile-dropdown/profile-dropdown.component';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MapService } from '../../google-maps/map.service';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  imports: [ProfileDropdownComponent, CommonModule, ReactiveFormsModule],
})
export class HeaderComponent {
  searchForm: FormGroup;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private mapService: MapService
  ) {
    this.searchForm = this.fb.group({
      location: [''],
    });
  }

  onNavigateHome() {
    this.router.navigate(['/home']);
  }

  onSubmit() {
    const location = this.searchForm.get('location')?.value;
    this.mapService.geocodeLocation(location).subscribe((response: any) => {
      if (response.status === 'OK') {
        const lat = response.results[0].geometry.location.lat;
        const lng = response.results[0].geometry.location.lng;
        this.mapService.setPosition(lat, lng);
        this.router.navigate(['/location', lat, lng]);
      }
    });
  }
}
