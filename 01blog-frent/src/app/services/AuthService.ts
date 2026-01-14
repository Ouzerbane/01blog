import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  http = inject(HttpClient)
  login(body: object) {
    const url = 'http://localhost:8080/login'
    return this.http.post<any>(url, body,{withCredentials:true}).pipe(
      catchError((err) => {
        return throwError(() => err)
      })
    )
  }

  register(body: object){
     const url = "http://localhost:8080/regester";
     return this.http.post<any>(url,body).pipe(
      catchError((err)=>{
        return throwError(()=> err);
      })
     )
  }
}
