import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostsResponse } from '../modules/home/home-module';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HommeService {

  constructor(private http: HttpClient) { }

  getAllPosts() {
    return this.http.get<PostsResponse>(`http://localhost:8080/post/get-posts`, { withCredentials: true })
      .pipe(
        catchError((error) => {
          return throwError(() => error);
        })
      );
  }

  getSuggestedUsers() {
    const suggestedUrl = `http://localhost:8080/suggested`;
    return this.http.get<{ data: any }>(suggestedUrl, { withCredentials: true })
      .pipe(
        catchError((error) => {
          return throwError(() => error);
        })
      );
  }


  followUser(userId: string) {
    console.log(userId.length);
    const followUrl = `http://localhost:8080/follow`;
    return this.http.post<any>(followUrl, { id: userId }, { withCredentials: true })
      .pipe(
        catchError((error) => {
          return throwError(() => error);
        })
      );
  }

  recherchUsers(searchTerm: string) {
    const searchUrl = `http://localhost:8080/serch?input=${searchTerm}`;
    return this.http.get<any>(searchUrl, { withCredentials: true })
      .pipe(
        catchError((error) => {
          return throwError(() => error);
        })
      );
  }
}