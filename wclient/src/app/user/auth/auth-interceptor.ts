import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, throwError } from 'rxjs';
import { StorageService } from '../storage/storage.service';
import { AuthStatusService } from './auth-status.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private authStatusService: AuthStatusService
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        const passwordChangeUrl = 'http://localhost:8080/api/users/';

        if (error.status === 401) {
          StorageService.logout();
          this.authStatusService.updateLoggedInStatus();
          this.router.navigate(['/login']);
        } else if (
          error.status === 403 &&
          req.url.includes(passwordChangeUrl)
        ) {
          if (error.error) {
            console.error('Password change failed:', error.error.error);
          } else {
            console.error('Password change failed:', error.message);
          }
        } 
        return throwError(() => error);
      })
    );
  }
}
