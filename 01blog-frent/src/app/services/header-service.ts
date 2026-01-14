import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HeaderService {
  constructor(private http: HttpClient) { }

  logout() {
    const url = 'http://localhost:8080/logout';
    return this.http.post(url, {}, { withCredentials: true }).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    )
  }

  GetNotification() {
    const url = `http://localhost:8080/notifications`;
    return this.http.get<any>(url, { withCredentials: true }).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    )
  }

  CountNotification() {
    const url = `http://localhost:8080/count-notifications`;
    return this.http.get<any>(url, { withCredentials: true }).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    )
  }

  NotificationRead(id: string) {
    const url = `http://localhost:8080/mark-notifications-read/${id}`;
    return this.http.put<any>(url, {}, { withCredentials: true }).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    )
  }

  getCurrentUserProfile() {
    const url = `http://localhost:8080/me`;
    return this.http.get<{ data: any }>(url, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  GetuserTypeService() {
    const url = `http://localhost:8080/admin/user-type`;
    return this.http.get<any>(url, { withCredentials: true }).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    )
  }
}
