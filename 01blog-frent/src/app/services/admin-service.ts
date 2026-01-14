import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdminService {

  constructor(private http: HttpClient) { }

  BanUserService(userId: string) {
    const url = `http://localhost:8080/admin/BanUser`;
    const body = { id: userId };

    const options = {
      withCredentials: true
    };

    return this.http.put(url, body, options).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    );
  }

  ChangeStatusService(userId: string) {
    const url = `http://localhost:8080/admin/changeStatus`;
    const body = { id: userId };

    const options = {
      withCredentials: true
    };

    return this.http.put(url, body, options).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    );
  }

  DeletePostService(postId: string) {
    const url = `http://localhost:8080/post/delete-post`;

    const options = {
      body: { id: postId },
      withCredentials: true // quest
    };

    return this.http.delete(url, options).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    );
  }

  DeletUserService(userId: string) {
    const url = `http://localhost:8080/admin/remove-user`;
    const options = {
      body: { id: userId },
      withCredentials: true
    };

    return this.http.delete(url, options).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    );
  }

  DeletReportService(reportId: string) {
    const url = `http://localhost:8080/admin/remove-report`;
    const options = {
      body: { id: reportId },
      withCredentials: true
    };

    return this.http.delete(url, options).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    );
  }

  getReportsService() {
    const api = `http://localhost:8080/admin/reports`;
    return this.http.get<any>(api, { withCredentials: true }).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    );
  }

  getUsersService() {
    const api = `http://localhost:8080/admin/users`;
    return this.http.get<any>(api, { withCredentials: true }).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    );
  }

  getPostsService() {
    const api = `http://localhost:8080/admin/posts`;
    return this.http.get<any>(api, { withCredentials: true }).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    );
  }
}
