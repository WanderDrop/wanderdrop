import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  HostListener,
  QueryList,
  Renderer2,
  ViewChildren,
} from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile-dropdown',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile-dropdown.component.html',
  styleUrl: './profile-dropdown.component.css',
})
export class ProfileDropdownComponent implements AfterViewInit {
  @ViewChildren('smallDropdownMenu') dropdownMenuSmall!: QueryList<ElementRef>;

  screenWidth: number = window.innerWidth;

  constructor(
    private renderer: Renderer2,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  @HostListener('window:resize')
  onResize() {
    this.screenWidth = window.innerWidth;
    this.adjustMenuClass();
    this.cdr.detectChanges();
  }

  ngAfterViewInit() {
    this.dropdownMenuSmall.changes.subscribe(() => {
      this.adjustMenuClass();
    });
  }

  adjustMenuClass() {
    const screenWidth = window.innerWidth;

    this.dropdownMenuSmall?.forEach((menu) => {
      const dropdownMenu = menu.nativeElement;

      if (
        (screenWidth >= 437 && screenWidth <= 490) ||
        (screenWidth >= 564 && screenWidth <= 575)
      ) {
        this.renderer.addClass(dropdownMenu, 'dropdown-menu-end');
        this.renderer.addClass(dropdownMenu, 'no-overlap');
      } else {
        this.renderer.removeClass(dropdownMenu, 'dropdown-menu-end');
        this.renderer.removeClass(dropdownMenu, 'no-overlap');
      }
    });
  }

  onDisplayProfile() {
    this.router.navigate(['/profile']);
  }

  onDisplayActivity() {}

  onDisplayReports() {
  }

  onLogout() {}

  onAddNewUser() {
    this.router.navigate(['/add-user']);
  }
}
