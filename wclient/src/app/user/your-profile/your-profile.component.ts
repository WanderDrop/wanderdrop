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
export class YourProfileComponent {
  profileForm!: FormGroup;
  originalValues: any;
  changePasswordForm!: FormGroup;
  @ViewChild('modal') modal!: TemplateRef<any>;
  isModalOpen = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private viewContainerRef: ViewContainerRef,
    private router: Router
  ) {
    const dummyUser = this.userService.getDummyUser();

    this.profileForm = this.fb.group({
      firstName: [dummyUser.firstName, Validators.required],
      lastName: [dummyUser.lastName, Validators.required],
      email: [{ value: dummyUser.email, disabled: true }, Validators.required],
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
    if (this.changePasswordForm.valid) {
      const dummyUser = this.userService.getDummyUser();
      dummyUser.password = this.changePasswordForm.value.newPassword;
      console.log(this.userService.getDummyUser().password);
      this.isModalOpen = false;
    }
  }

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

  close() {
    this.router.navigate(['home']);
  }

  save() {
    if (this.profileForm.valid) {
      const dummyUser = this.userService.getDummyUser();
      dummyUser.firstName = this.profileForm.value.firstName;
      dummyUser.lastName = this.profileForm.value.lastName;
    }
  }
}
