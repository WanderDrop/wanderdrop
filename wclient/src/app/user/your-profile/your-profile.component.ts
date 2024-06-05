import { CommonModule } from '@angular/common';
import {
  Component,
  OnInit,
  TemplateRef,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserService } from '../user.service';
import { Router } from '@angular/router';

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
    private viewContainerRef: ViewContainerRef,
    private router: Router
  ) {
    const currentUser = this.userService.getCurrentUser();

    this.profileForm = this.fb.group({
      firstName: [currentUser.Firstname, Validators.required],
      lastName: [currentUser.Lastname, Validators.required],
      email: [
        { value: currentUser.Email, disabled: true },
        Validators.required,
      ],
    });
    this.originalValues = this.profileForm.value;
  }

  ngOnInit(): void {
    this.changePasswordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
    });
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
            if (error.status === 401) {
              this.passwordError = 'The current password does not match.';
              this.changePasswordForm.controls['currentPassword'].reset();
            } else {
              console.log('Error occurred');
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
        currentUser.Firstname !== this.profileForm.value.firstName ||
        currentUser.Lastname !== this.profileForm.value.lastName
      ) {
        currentUser.Firstname = this.profileForm.value.firstName;
        currentUser.Lastname = this.profileForm.value.lastName;
        this.showNameChangeSuccessMessage = true;
        setTimeout(() => (this.showNameChangeSuccessMessage = false), 2500);
      }
    }
  }
}
