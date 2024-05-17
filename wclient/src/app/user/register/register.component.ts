import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
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

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  registerForm!: FormGroup;

  constructor(private router: Router, private userService: UserService) {}

  ngOnInit() {
    this.registerForm = new FormGroup({
      firstName: new FormControl(null, Validators.required),
      lastName: new FormControl(null, Validators.required),
      email: new FormControl(null, [Validators.required, Validators.email]),
      userPassword: new FormControl(null, Validators.required),
      agreeToTerms: new FormControl(false, Validators.requiredTrue),
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const newUser = new User(
        this.registerForm.value.firstName,
        this.registerForm.value.lastName,
        this.registerForm.value.email,
        this.registerForm.value.userPassword,
        UserRole.USER
      );
      this.userService.registerUser(newUser).subscribe(
        (response) => {
          console.log(response);
          this.router.navigate(['/home']);
        },
        (error) => {
          console.error(error);
        }
      );
    } else {
      this.registerForm.markAllAsTouched();
    }
  }

  onNavigateHome() {
    this.router.navigate(['/home']);
  }
}
