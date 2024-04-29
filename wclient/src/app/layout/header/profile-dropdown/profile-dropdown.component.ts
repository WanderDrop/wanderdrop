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

@Component({
  selector: 'app-profile-dropdown',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile-dropdown.component.html',
  styleUrl: './profile-dropdown.component.css',
})
export class ProfileDropdownComponent implements AfterViewInit {
  @ViewChildren('smallDropdownMenu') ddmenusmall!: QueryList<ElementRef>;
  @ViewChildren('largeDropdownMenu') ddmenularge!: QueryList<ElementRef>;
  screenWidth: number = window.innerWidth;

  constructor(private renderer: Renderer2, private cdr: ChangeDetectorRef) {}

  @HostListener('window:resize')
  onResize() {
    this.screenWidth = window.innerWidth;
    this.adjustMenuClass();
    this.cdr.detectChanges();
  }

  ngAfterViewInit() {
    this.ddmenusmall.changes.subscribe(() => {
      this.adjustMenuClass();
    });

    this.ddmenularge.changes.subscribe(() => {
      this.adjustMenuClass();
    });
  }

  adjustMenuClass() {
    const screenWidth = window.innerWidth;
    console.log('Screen width: ', screenWidth);

    this.ddmenusmall?.forEach((menu) => {
      const dropdownMenu = menu.nativeElement;

      if (
        (screenWidth >= 437 && screenWidth <= 490) ||
        (screenWidth >= 564 && screenWidth <= 575)
      ) {
        this.renderer.addClass(dropdownMenu, 'dropdown-menu-end');
        this.renderer.addClass(dropdownMenu, 'no-overlap');
        console.log('Classes added');
      } else {
        this.renderer.removeClass(dropdownMenu, 'dropdown-menu-end');
        this.renderer.removeClass(dropdownMenu, 'no-overlap');
        console.log('Classes removed');
      }

      // Log the classes of the dropdown menu
      console.log('Small dropdown menu classes: ', dropdownMenu.classList);
    });

    this.ddmenularge?.forEach((menu) => {
      const dropdownMenu = menu.nativeElement;

      if (screenWidth > 563 && screenWidth < 576) {
        this.renderer.removeClass(dropdownMenu, 'dropdown-menu-end');
        console.log('Class removed from large menu');
      }

      // Log the classes of the dropdown menu
      console.log('Large dropdown menu classes: ', dropdownMenu.classList);
    });
  }

  onDisplayProfile() {}

  onDisplayActivity() {}

  onDisplayReports() {}

  onLogout() {}
}
