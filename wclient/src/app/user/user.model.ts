import { UserRole } from './user-role.enum';

export class User {
  private userId!: number;
  private email: string;
  private password: string;
  private firstname: string;
  private lastname: string;
  private role: UserRole;
  public static lastId = 0;

  constructor(
    firstname: string,
    lastname: string,
    email: string,
    password: string,
    role: UserRole
  ) {
    User.lastId++;
    this.userId = User.lastId;
    this.password = password;
    this.email = email;
    this.firstname = firstname;
    this.lastname = lastname;
    this.role = role;
  }

  get UserId(): number {
    return this.userId;
  }

  get Email(): string {
    return this.email;
  }

  set Email(value: string) {
    this.email = value;
  }

  get Password(): string {
    return this.password;
  }

  set Password(value: string) {
    this.password = value;
  }

  get Firstname(): string {
    return this.firstname;
  }

  set Firstname(value: string) {
    this.firstname = value;
  }

  get Lastname(): string {
    return this.lastname;
  }

  set Lastname(value: string) {
    this.lastname = value;
  }

  get Role(): UserRole {
    return this.role;
  }

  set Role(value: UserRole) {
    this.role = value;
  }
}
