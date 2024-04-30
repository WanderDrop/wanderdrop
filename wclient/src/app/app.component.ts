import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { HeaderComponent } from './layout/header/header.component';
import { FooterComponent } from './layout/footer/footer.component';
import { GoogleMapsComponent } from './google-maps/google-maps.component';

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
export class AppComponent {
  title = 'wclient';

  constructor(private router: Router) {}

  register() {
    this.router.navigate(['register']);
  }
}
