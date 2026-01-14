import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PostActions {
  constructor(private http: HttpClient) { }

  likePost(postId: string) {
    const url = `http://localhost:8080/post/like-post`;
    const body = { id: postId };

    return this.http.post<any>(url, body, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      }
      ));
  }

  getComments(postId: string) {
    const url = `http://localhost:8080/post/get-comments?postId=${postId}`;
    return this.http.get<any>(url, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  deleteComment(postId: string, commentId: string) {

    const url = `http://localhost:8080/post/delete-comment`;
    const options = {
      body: { id: commentId, postId: postId },
      withCredentials: true // quest
    };

    return this.http.delete<any>(url, options).pipe(
      catchError((error) => {
        return throwError(() => error);
      }
      ));
  }


  addComment(content: string, id: string) {
    const url = `http://localhost:8080/post/add-comments`;
    const body = {
      id: id,
      content: content
    };
    return this.http.post<any>(url, body, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  deletePost(postId: string) {
    const url = `http://localhost:8080/post/delete-post`;
    const options = {
      body: { id: postId },
      withCredentials: true // quest
    };

    return this.http.delete<any>(url, options).pipe(
      catchError((error) => {
        return throwError(() => error);
      }
      ));
  }

  editPost(formData: FormData) {
    const url = `http://localhost:8080/post/edit-post`;
    return this.http.put<any>(url, formData, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      }
      ));
  }

  reportPost(postId: string, reason: string) {
    const url = `http://localhost:8080/report-post`;
    const body = {
      targetPostId: postId,
      reason: reason
    };
    return this.http.post<any>(url, body, { withCredentials: true }).pipe(
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }
}
