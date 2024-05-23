import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  HostListener,
  OnDestroy,
  QueryList,
  Renderer2,
  ViewChildren,
} from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthStatusService } from '../../../user/auth/auth-status.service';
import { StorageService } from '../../../user/storage/storage.service';

@Component({
  selector: 'app-profile-dropdown',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile-dropdown.component.html',
  styleUrl: './profile-dropdown.component.css',
})
export class ProfileDropdownComponent implements AfterViewInit, OnDestroy {
  @ViewChildren('smallDropdownMenu') dropdownMenuSmall!: QueryList<ElementRef>;
  screenWidth: number = window.innerWidth;
  private subscriptions: Subscription[] = [];

  constructor(
    private renderer: Renderer2,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private authStatusService: AuthStatusService
  ) {}

  @HostListener('window:resize')
  onResize() {
    this.screenWidth = window.innerWidth;
    this.adjustMenuClass();
    this.cdr.detectChanges();
  }

  ngAfterViewInit() {
    const dropdownMenuSmallChangesSub =
      this.dropdownMenuSmall.changes.subscribe(() => {
        this.adjustMenuClass();
      });
    this.subscriptions.push(dropdownMenuSmallChangesSub);
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

  onDisplayActivity() {
    this.router.navigate(['activity']);
  }

  onDisplayReports() {}

  onLogout() {
    StorageService.logout();

    this.authStatusService.logout();
    this.router.navigate(['/login']);
  }

  onAddNewUser() {
    this.router.navigate(['/add-user']);
  }

  ngOnDestroy() {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
