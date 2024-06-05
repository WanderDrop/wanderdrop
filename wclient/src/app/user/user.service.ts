import { Injectable } from '@angular/core';
import { User } from './user.model';
import { UserRole } from './user-role.enum';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from './storage/storage.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  // private users: User[] = [];

  constructor(private http: HttpClient) {
    // const dummyUser = new User(
    //   'Dummy',
    //   'User',
    //   'dummy@example.com',
    //   'password',
    //   UserRole.USER
    // );
    // this.users.push(dummyUser);
  }

  registerUser(user: User) {
    return this.http.post('http://localhost:8080/api/auth/register', user);
  }

  changePassword(oldPassword: string, newPassword: string): Observable<any> {
    const token = StorageService.getToken();
    const user = StorageService.getUser();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `http://localhost:8080/api/users/${user.userId}/change-password`;
    const body = { oldPassword, newPassword };
    return this.http.post(url, body, { headers });
  }

  // addUser(user: User) {
  //   this.users.push(user);
  // }

  // getUsers(): User[] {
  //   return [...this.users];
  // }

  // getDummyUser(): User {
  //   return this.users[0];
  // }

  getCurrentUser(): User {
    return StorageService.getUser();
  }

  // verifyPassword(inputPassword: string): boolean {
  //   const dummyUser = this.getDummyUser();
  //   return dummyUser.Password === inputPassword;
  // }
}
