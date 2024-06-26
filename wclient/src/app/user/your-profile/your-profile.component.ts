import { CommonModule } from '@angular/common';
import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { StorageService } from '../storage/storage.service';

@Component({
  selector: 'app-your-profile',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './your-profile.component.html',
  styleUrl: './your-profile.component.css',
})
export class YourProfileComponent implements OnInit {
  profileForm!: FormGroup;
  originalValues: any;
  changePasswordForm!: FormGroup;
  @ViewChild('modal') modal!: TemplateRef<any>;
  isModalOpen = false;
  passwordError = '';
  newPasswordError = '';
  showSuccessMessage = false;
  showNameChangeSuccessMessage = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    try {
      const currentUser = this.userService.getCurrentUser();
      console.log('Current user:', currentUser);

      if (currentUser) {
        this.profileForm = this.fb.group({
          firstName: [currentUser.firstName, Validators.required],
          lastName: [currentUser.lastName, Validators.required],
          email: [
            { value: currentUser.email, disabled: true },
            Validators.required,
          ],
        });
        this.originalValues = this.profileForm.value;
      } else {
        this.router.navigate(['login']);
      }

      this.changePasswordForm = this.fb.group({
        currentPassword: ['', Validators.required],
        newPassword: ['', Validators.required],
      });
    } catch (error) {
      console.error('Error fetching current user:', error);
      this.router.navigate(['login']);
    }
  }

  valuesChanged(): boolean {
    return (
      JSON.stringify(this.profileForm.value) !==
      JSON.stringify(this.originalValues)
    );
  }

  changePassword() {
    this.openModal();
  }

  submitChangePassword() {
    this.newPasswordError = '';
    this.passwordError = '';
    if (this.changePasswordForm.valid) {
      const inputPassword = this.changePasswordForm.value.currentPassword;
      const newPassword = this.changePasswordForm.value.newPassword;
      if (inputPassword === newPassword) {
        this.newPasswordError =
          'The new password should not be the same as the old password.';
        this.changePasswordForm.controls['newPassword'].reset();
      } else {
        this.userService.changePassword(inputPassword, newPassword).subscribe({
          next: () => {
            this.showSuccessMessage = true;
            setTimeout(() => {
              this.showSuccessMessage = false;
              this.isModalOpen = false;
              this.changePasswordForm.reset();
            }, 2500);
          },
          error: (error) => {
            console.log(error.message);
            if (
              error.message.includes(
                'Http failure response for http://localhost:8080/api/users'
              )
            ) {
              this.passwordError = 'The current password does not match.';
              this.changePasswordForm.controls['currentPassword'].reset();
              this.changePasswordForm.controls['newPassword'].reset();
            } else {
              console.log('Error occurred:', error);
            }
          },
        });
      }
    }
  }

  openModal() {
    this.isModalOpen = true;
    this.passwordError = '';
    this.newPasswordError = '';
    this.changePasswordForm.reset();
  }

  closeModal() {
    this.isModalOpen = false;
  }

  close() {
    this.router.navigate(['home']);
  }

  save() {
    if (this.profileForm.valid) {
      const currentUser = this.userService.getCurrentUser();
      if (
        currentUser.firstName !== this.profileForm.value.firstName ||
        currentUser.lastName !== this.profileForm.value.lastName
      ) {
        const updatedUser = {
          firstName: this.profileForm.value.firstName,
          lastName: this.profileForm.value.lastName,
        };
        this.userService.updateUser(currentUser.userId, updatedUser).subscribe({
          next: () => {
            currentUser.firstName = updatedUser.firstName;
            currentUser.lastName = updatedUser.lastName;
            StorageService.saveUser(currentUser);
            this.showNameChangeSuccessMessage = true;
            setTimeout(() => (this.showNameChangeSuccessMessage = false), 2500);
          },
          error: (error) => {
            console.error('Error updating profile', error);
          },
        });
      }
    }
  }
}
