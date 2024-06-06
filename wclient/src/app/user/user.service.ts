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
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  registerUser(user: User) {
    return this.http.post(`${this.apiUrl}/auth/register`, user);
  }

  changePassword(oldPassword: string, newPassword: string): Observable<any> {
    const token = StorageService.getToken();
    const user = StorageService.getUser();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `${this.apiUrl}/users/${user.userId}/change-password`;
    const body = { oldPassword, newPassword };
    return this.http.post(url, body, { headers });
  }

  getCurrentUser(): User {
    const user = StorageService.getUser();
    if (!user) {
      throw new Error('User is not defined in local storage');
    }
    return user;
  }

  updateUser(userId: string, user: any): Observable<any> {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put(`${this.apiUrl}/users/${userId}/update`, user, {
      headers,
    });
  }
}
