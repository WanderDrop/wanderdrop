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
    console.log('Interceptor executed');
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          StorageService.logout();
          this.authStatusService.updateLoggedInStatus();
          this.router.navigate(['/login']);
          console.log('TEST');
        }
        return throwError(() => error);
      })
    );
  }
}
