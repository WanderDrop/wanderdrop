import { Component, OnInit } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { HeaderComponent } from './layout/header/header.component';
import { FooterComponent } from './layout/footer/footer.component';
import { GoogleMapsComponent } from './google-maps/google-maps.component';
import { LoginComponent } from './user/login/login.component';
import { AuthStatusService } from './user/auth/auth-status.service';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [
    RouterOutlet,
    RouterModule,
    HeaderComponent,
    FooterComponent,
    GoogleMapsComponent,
  ],
})
export class AppComponent implements OnInit {
  title = 'wclient';

  constructor(
    private router: Router,
    private authStatusService: AuthStatusService
  ) {}

  ngOnInit() {
    this.authStatusService.updateLoggedInStatus();
  }

  register() {
    this.router.navigate(['register']);
  }
  login() {
    this.router.navigate(['login']);
  }
}
