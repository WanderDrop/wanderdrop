import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthStatusService } from './auth-status.service';
import { LoginResponse } from './login-response';

const BASE_URL = ['http://localhost:8080'];

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private http: HttpClient,
    private authStatusService: AuthStatusService
  ) {}

  login(loginRequest: any): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(BASE_URL + '/api/auth/login', loginRequest)
      .pipe(
        tap((response) => {
          this.authStatusService.updateLoginStatus(true);
          this.authStatusService.updateUserRole(response.role);
        })
      );
  }

  logout() {
    this.authStatusService.logout();
  }
}
