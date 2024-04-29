import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ProfileDropdownComponent } from './profile-dropdown/profile-dropdown.component';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  imports: [ProfileDropdownComponent],
})
export class HeaderComponent {
  constructor(private router: Router) {}

  onNavigateHome() {
    this.router.navigate(['/home']);
  }
}
