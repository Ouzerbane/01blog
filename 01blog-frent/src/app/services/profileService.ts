import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { PostsResponse } from '../modules/home/home-module';
import { SuggestedResponse } from '../modules/profile/profile-module';

@Injectable({
  providedIn: 'root',
})
export class profileService {

  constructor(private http: HttpClient) { }

  grtUserprofile(id: string) {
    const url = `http://localhost:8080/get-userInfo/${id}`;
    return this.http.get<{ data: any }>(url, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  getPostsProfile(id: string) {
    const url = `http://localhost:8080/get-post-profile/${id}`;
    return this.http.get<PostsResponse>(url, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  reportUser(reason: string, userId: string) {
    const body = { reason: reason, targetUserId: userId };
    const url = 'http://localhost:8080/report-user';
    return this.http.post(url, body, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  uploadProfilePicture(metaData: FormData) {
    const url = "http://localhost:8080/update-profile-image";
    return this.http.post<any>(url, metaData, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }


  getFollowers(userId: string) {
    const url = `http://localhost:8080/get-Followers/${userId}`;
    return this.http.get<SuggestedResponse>(url, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  getFollowing(userId: string) {
    const url = `http://localhost:8080/get-Following/${userId}`;
    return this.http.get<SuggestedResponse>(url, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  toggleFollowUser(targetUserId: string) {
    const url = `http://localhost:8080/follow`;
    return this.http.post<any>(url, { id : targetUserId }, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

}
