import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { AuthService } from '../auth/auth.service';
import { StorageService } from '../storage/storage.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
onSubmit() {
throw new Error('Method not implemented.');
} 
  loginForm!: FormGroup;
  registerForm: any;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
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
        userId: res.userId,
        role: res.role
       };
      StorageService.saveUser(user);
      StorageService.saveToken(res.token);
      if (StorageService.isAdminLoggedIn()) {
        this.router.navigateByUrl("/home");
      } else if (StorageService.isUserLoggedIn()) {
        this.router.navigateByUrl("/home");
      } else {
      console.error("Bad credentials");
      } 
     }
    });
  }

  onNavigateHome() {
    this.router.navigate(['/home']);
  }

 onRegister(){
  this.router.navigate(['/register']);
 }
}
