import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Guard {
  constructor(private router: Router, private http: HttpClient) { }

  canActivate() {
    const url = 'http://localhost:8080/me';
    return this.http.get<any>(url, { withCredentials: true })
  }

  canActivateAuther() {
    const url = 'http://localhost:8080/me';
    return this.http.get<any>(url, { withCredentials: true })
  }

  canActivateAdmin() {
    const url = `http://localhost:8080/admin/user-type`;

    return this.http.get<any>(url, { withCredentials: true })
  }

}