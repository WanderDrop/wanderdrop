import { Injectable } from '@angular/core';
import { User } from './user.model';
import { UserRole } from './user-role.enum';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private users: User[] = [];

  constructor() {
    const dummyUser = new User(
      'dummy@example.com',
      'password',
      'Dummy',
      'User',
      UserRole.User
    );
    this.users.push(dummyUser);
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
    return dummyUser.password === inputPassword;
  }
}
