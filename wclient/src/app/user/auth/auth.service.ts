import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const BASE_URL = ["http://localhost:8080"]; // to connect with Backend 

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http:HttpClient) {}

  login(loginRequest:any):Observable<any>{
    return this.http.post(BASE_URL + "/api/auth/login", loginRequest);
  }
}
