import { Routes } from '@angular/router';
import { RegisterComponent } from './user/register/register.component';
import { AttractionComponent } from './attraction/attraction.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'attraction', component: AttractionComponent },
];
