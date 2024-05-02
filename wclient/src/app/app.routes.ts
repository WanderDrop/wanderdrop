import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './user/register/register.component';
import { AttractionComponent } from './attraction/attraction.component';
import { GoogleMapsComponent } from './google-maps/google-maps.component';
import { NgModule } from '@angular/core';
import { AddUserComponent } from './user/add-user/add-user.component';

export const routes: Routes = [
  { path: '', component: GoogleMapsComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'attraction', component: AttractionComponent },
  { path: 'home', component: GoogleMapsComponent },
  { path: 'add-user', component: AddUserComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
