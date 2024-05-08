import { UserRole } from './user-role.enum';

export class User {
  private _userId!: number;
  private _email: string;
  private _password: string;
  private _firstName: string;
  private _lastName: string;
  private _roleId: UserRole;
  private _createdAt: Date;
  private _updatedAt?: Date;
  private _status: string;
  public static lastId = 0;

  constructor(
    email: string,
    password: string,
    firstName: string,
    lastName: string,
    roleId: UserRole = UserRole.User
  ) {
    User.lastId++;
    this._userId = User.lastId;
    this._password = password;
    this._email = email;
    this._firstName = firstName;
    this._lastName = lastName;
    this._roleId = roleId;
    this._createdAt = new Date();
    this._status = 'active';
  }

  get userId(): number {
    return this._userId;
  }

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get password(): string {
    return this._password;
  }

  set password(value: string) {
    this._password = value;
  }

  get firstName(): string {
    return this._firstName;
  }

  set firstName(value: string) {
    this._firstName = value;
  }

  get lastName(): string {
    return this._lastName;
  }

  set lastName(value: string) {
    this._lastName = value;
  }

  get roleId(): UserRole {
    return this._roleId;
  }

  set roleId(value: UserRole) {
    this._roleId = value;
  }

  get createdAt(): Date {
    return this._createdAt;
  }

  get updatedAt(): Date | undefined {
    return this._updatedAt;
  }

  set updatedAt(value: Date) {
    this._updatedAt = value;
  }

  get status(): string {
    return this._status;
  }

  set status(value: string) {
    this._status = value;
  }
}
