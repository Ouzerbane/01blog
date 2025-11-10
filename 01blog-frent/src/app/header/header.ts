import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule,HttpClientModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class Header implements OnInit {
  userId: number = 0;
  username: string = '';

  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getCurrentUser().subscribe({
      next: (res) => {
        // if (res?.success && res.data) {
          this.userId = res.data.id;
          console.log("=====>",res);
          
          this.username = res.data.username;
        // }
      },
      error: (err) => {
        console.error('Error fetching user info:', err);
      },
    });
  }

 
  getCurrentUser(): Observable<any> {
    return this.http.get(`${this.apiUrl}/me`, { withCredentials: true });
  }
}
