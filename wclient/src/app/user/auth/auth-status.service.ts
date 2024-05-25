import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthStatusService {
  private loggedInStatus = new BehaviorSubject<boolean>(false);
  private userRoleStatus = new BehaviorSubject<string>('');

  constructor() {}

  loggedInStatus$ = this.loggedInStatus.asObservable();
  userRoleStatus$ = this.userRoleStatus.asObservable();

  updateLoginStatus(isLoggedIn: boolean) {
    this.loggedInStatus.next(isLoggedIn);
  }

  updateUserRole(role: string) {
    this.userRoleStatus.next(role);
  }

  logout() {
    this.loggedInStatus.next(false);
    this.userRoleStatus.next('');
  }
}
