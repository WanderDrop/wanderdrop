import {
  AfterViewInit,
  Component,
  ElementRef,
  NgZone,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ProfileDropdownComponent } from './profile-dropdown/profile-dropdown.component';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MapService } from '../../google-maps/map.service';
import { Subscription } from 'rxjs';
import { AuthStatusService } from '../../user/auth/auth-status.service';
import { StorageService } from '../../user/storage/storage.service';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  imports: [
    ProfileDropdownComponent,
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
  ],
})
export class HeaderComponent implements OnInit, AfterViewInit, OnDestroy {
  searchForm: FormGroup;
  @ViewChild('search', { static: false })
  public searchElementRef!: ElementRef;
  private autocompleteInitialized = false;

  isLoggedIn = false;
  isAdmin = false;

  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private mapService: MapService,
    private ngZone: NgZone,
    private authStatusService: AuthStatusService
  ) {
    this.searchForm = this.fb.group({
      location: [''],
    });
  }

  ngOnInit() {
    this.isLoggedIn = !!StorageService.getToken();
    this.isAdmin = StorageService.getUserRole() === 'ADMIN';

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

    const loginStatusSub = this.authStatusService.loggedInStatus$.subscribe(
      (isLoggedIn) => {
        this.isLoggedIn = isLoggedIn;
      }
    );
    this.subscriptions.push(loginStatusSub);

    const userRoleSub = this.authStatusService.userRoleStatus$.subscribe(
      (role) => {
        this.isAdmin = role === 'ADMIN';
      }
    );
    this.subscriptions.push(userRoleSub);
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
