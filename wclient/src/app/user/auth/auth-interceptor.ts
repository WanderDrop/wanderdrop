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
        const passwordChangeUrl = '/api/users/change-password';

        if (error.status === 401) {
          if (req.url.includes(passwordChangeUrl)) {
            return throwError(() => error);
          } else {
            StorageService.logout();
            this.authStatusService.updateLoggedInStatus();
            this.router.navigate(['/login']);
          }
        }

        return throwError(() => error);
      })
    );
  }
}
