import {
  AfterViewInit,
  Component,
  ElementRef,
  NgZone,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';
import { ProfileDropdownComponent } from './profile-dropdown/profile-dropdown.component';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MapService } from '../../google-maps/map.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  imports: [ProfileDropdownComponent, CommonModule, ReactiveFormsModule],
})
export class HeaderComponent implements OnInit, AfterViewInit, OnDestroy {
  searchForm: FormGroup;
  @ViewChild('search', { static: false })
  public searchElementRef!: ElementRef;
  private autocompleteInitialized = false;
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private mapService: MapService,
    private ngZone: NgZone
  ) {
    this.searchForm = this.fb.group({
      location: [''],
    });
  }

  ngOnInit() {
    const resetSearchFormSub = this.mapService
      .getResetSearchForm()
      .subscribe((reset) => {
        if (reset) {
          this.searchForm.reset();
          const autocompleteInput = document.getElementById(
            'search-input'
          ) as HTMLInputElement;
          autocompleteInput.value = '';
        }
      });
    this.subscriptions.push(resetSearchFormSub);
  }

  ngAfterViewInit() {
    if (!this.autocompleteInitialized) {
      this.setupAutocomplete();
    }
  }

  setupAutocomplete() {
    this.mapService.loadGoogleMaps().then(() => {
      const autocomplete = new google.maps.places.Autocomplete(
        this.searchElementRef.nativeElement
      );

      autocomplete.addListener('place_changed', () => {
        this.ngZone.run(() => {
          const place: google.maps.places.PlaceResult = autocomplete.getPlace();

          if (place.geometry && place.geometry.location) {
            const lat = place.geometry.location.lat();
            const lng = place.geometry.location.lng();
            this.mapService.setPosition(lat, lng);
          }
        });
      });

      this.autocompleteInitialized = true;
    });
  }

  onNavigateHome() {
    this.router.navigate(['/home']);
  }

  onSubmit() {
    const location = this.searchForm.get('location')?.value;
    const geocodeLocationSub = this.mapService
      .geocodeLocation(location)
      .subscribe((response: any) => {
        if (response.status === 'OK') {
          const lat = response.results[0].geometry.location.lat;
          const lng = response.results[0].geometry.location.lng;
          this.mapService.setPosition(lat, lng);
        }
        const autocompleteInput = document.getElementById(
          'search-input'
        ) as HTMLInputElement;
        autocompleteInput.value = '';
        this.searchForm.get('location')?.setValue('');
      });
    this.subscriptions.push(geocodeLocationSub);
  }

  ngOnDestroy() {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
