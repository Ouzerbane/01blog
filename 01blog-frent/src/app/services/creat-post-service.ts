import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CreatPostService {

  constructor(private http: HttpClient) { }

  savepost(body: any) {
    const url = "http://localhost:8080/post/add-post";
    return this.http.post<any>(url, body, {withCredentials: true}).pipe(
      catchError((err) => {
        return throwError(() => err);
      })
    )
  }
}
