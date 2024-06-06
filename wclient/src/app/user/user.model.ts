import { UserRole } from './user-role.enum';

export class User {
  userId!: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: UserRole;

  constructor(
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    role: UserRole
  ) {
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
  }
}
