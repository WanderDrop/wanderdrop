import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './user/register/register.component';
import { AttractionComponent } from './attraction/attraction.component';
import { GoogleMapsComponent } from './google-maps/google-maps.component';
import { NgModule } from '@angular/core';

export const routes: Routes = [
  { path: '', component: GoogleMapsComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'attraction', component: AttractionComponent },
  { path: 'home', component: GoogleMapsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
