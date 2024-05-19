import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { AuthService } from '../auth/auth.service';
import { StorageService } from '../storage/storage.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent { 
  isSpinning: boolean = false;
  loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private userService: UserService,
  ) {}

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]]
    });
  }

  login() {
    this.authService.login(this.loginForm.value).subscribe((res: any) => {
      console.log(res);
      if (res.userId != null) {
        const user = {
          id: res.userId,
          role: res.userRole
        };
        StorageService.saveUser(user);
        StorageService.saveToken(res.jwt);
        if (StorageService.isAdminLoggedIn()) {
          this.router.navigateByUrl("/admin/home");
        } else if (StorageService.isUserLoggedIn()) {
          this.router.navigateByUrl("/user/home");
        } else {
          console.error("Bad credentials");
        }
      }
    });
  }

  onNavigateHome() {
    this.router.navigate(['/home']);
  }
}

