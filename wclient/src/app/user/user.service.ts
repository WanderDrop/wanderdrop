import { Injectable } from '@angular/core';
import { User } from './user.model';
import { UserRole } from './user-role.enum';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private users: User[] = [];

  constructor(private http: HttpClient) {
    const dummyUser = new User(
      'Dummy',
      'User',
      'dummy@example.com',
      'password',
      UserRole.USER
    );
    this.users.push(dummyUser);
  }

  registerUser(user: User) {
    return this.http.post('http://localhost:8080/api/auth/register', user);
  }

  addUser(user: User) {
    this.users.push(user);
  }

  getUsers(): User[] {
    return [...this.users];
  }

  getDummyUser(): User {
    return this.users[0];
  }

  verifyPassword(inputPassword: string): boolean {
    const dummyUser = this.getDummyUser();
    return dummyUser.Password === inputPassword;
  }
}
