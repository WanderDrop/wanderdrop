import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './user/register/register.component';
import { AttractionComponent } from './attraction/attraction.component';
import { GoogleMapsComponent } from './google-maps/google-maps.component';
import { NgModule } from '@angular/core';
import { AddUserComponent } from './user/add-user/add-user.component';
import { AddNewAttractionComponent } from './attraction/add-new-attraction/add-new-attraction.component';
import { YourProfileComponent } from './user/your-profile/your-profile.component';
import { ReportPageComponent } from './report-page/report-page.component';
import { AddNewReportPageComponent } from './report-page/add-new-report-page/add-new-report-page.component';
import { YourActivityComponent } from './user/your-activity/your-activity.component';
import { LoginComponent } from './user/login/login.component';

export const routes: Routes = [
  { path: '', component: GoogleMapsComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'attraction', component: AttractionComponent },
  { path: 'home', component: GoogleMapsComponent },
  { path: 'add-user', component: AddUserComponent },
  { path: 'add-new-attraction', component: AddNewAttractionComponent },
  { path: 'attraction/:id', component: AttractionComponent },
  { path: 'profile', component: YourProfileComponent },
  { path: 'reportPage', component: ReportPageComponent },
  { path: 'add-new-report-page', component: AddNewReportPageComponent },
  { path: 'reportPage/:id', component: ReportPageComponent },
  { path: 'activity', component: YourActivityComponent },
  { path: 'login', component: LoginComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
