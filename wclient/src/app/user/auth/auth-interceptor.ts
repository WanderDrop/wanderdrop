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

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    console.log('Interceptor executed');
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        console.log('HTTP Error intercepted:', error);
        if (error.status === 401) {
          console.log('401 Unauthorized Error');
          console.log('JWT token has expired, redirecting to login...');
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}
