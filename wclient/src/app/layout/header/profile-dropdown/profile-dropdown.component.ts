import { Component } from '@angular/core';

@Component({
  selector: 'app-profile-dropdown',
  standalone: true,
  imports: [],
  templateUrl: './profile-dropdown.component.html',
  styleUrl: './profile-dropdown.component.css',
})
export class ProfileDropdownComponent {
  onDisplayProfile() {}

  onDisplayActivity() {}

  onDisplayReports() {}

  onLogout() {}
}
