import { Routes } from '@angular/router';
import { RegisterComponent } from './user/register/register.component';
import { AttractionComponent } from './attraction/attraction.component';
import { GoogleMapsComponent } from './google-maps/google-maps.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'attraction', component: AttractionComponent },
  { path: 'home', component: GoogleMapsComponent },
];
