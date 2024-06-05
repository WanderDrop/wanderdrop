import { CommonModule } from '@angular/common';
import { Component, HostListener, OnDestroy } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { UserRole } from '../user-role.enum';
import { User } from '../user.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-add-user',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-user.component.html',
  styleUrl: './add-user.component.css',
})
export class AddUserComponent implements OnDestroy {
  registerForm!: FormGroup;
  isMobile!: boolean;
  showSuccessMessage = false;
  private subscriptions: Subscription[] = [];

  constructor(private router: Router, private userService: UserService) {
    this.isMobile = window.innerWidth <= 1100;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: { target: { innerWidth: number } }) {
    this.isMobile = event.target.innerWidth <= 1100;
  }

  ngOnInit() {
    this.registerForm = new FormGroup({
      firstName: new FormControl(null, Validators.required),
      lastName: new FormControl(null, Validators.required),
      email: new FormControl(null, [Validators.required, Validators.email]),
      userPassword: new FormControl(null, Validators.required),
      agreeToTerms: new FormControl(false, Validators.requiredTrue),
      role: new FormControl(null, Validators.required),
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const newUser = new User(
        this.registerForm.value.firstName,
        this.registerForm.value.lastName,
        this.registerForm.value.email,
        this.registerForm.value.userPassword,
        this.registerForm.value.role || UserRole.USER
      );
      const registerUserSub = this.userService.registerUser(newUser).subscribe({
        next: (response) => {
          console.log(response);
          this.showSuccessMessage = true;
          setTimeout(() => {
            this.router.navigate(['/home']);
          }, 1500);
        },
        error: (error) => {
          console.error(error);
        },
      });
      this.subscriptions.push(registerUserSub);
    } else {
      this.registerForm.markAllAsTouched();
    }
  }

  onNavigateHome() {
    this.router.navigate(['/home']);
  }

  ngOnDestroy() {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
