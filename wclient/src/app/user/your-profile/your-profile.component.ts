import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserService } from '../user.service';

@Component({
  selector: 'app-your-profile',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './your-profile.component.html',
  styleUrl: './your-profile.component.css',
})
export class YourProfileComponent {
  profileForm!: FormGroup;
  originalValues: any;

  constructor(private fb: FormBuilder, private userService: UserService) {
    const dummyUser = this.userService.getDummyUser();

    this.profileForm = this.fb.group({
      firstName: [dummyUser.firstName, Validators.required],
      lastName: [dummyUser.lastName, Validators.required],
      email: [{ value: dummyUser.email, disabled: true }, Validators.required],
    });
    this.originalValues = this.profileForm.value;
  }

  valuesChanged(): boolean {
    return (
      JSON.stringify(this.profileForm.value) !==
      JSON.stringify(this.originalValues)
    );
  }

  changePassword() {}

  close() {}

  save() {
    if (this.profileForm.valid) {
      const dummyUser = this.userService.getDummyUser();
      dummyUser.firstName = this.profileForm.value.firstName;
      dummyUser.lastName = this.profileForm.value.lastName;
    }
  }
}
