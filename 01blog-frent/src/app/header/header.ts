import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostService } from '../services/post';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class Header implements OnInit {
  userId: number = 0;
  username: string = '';

  private apiUrl = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
    public service: PostService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getCurrentUser().subscribe({
      next: (res) => {
        if (res?.data) {
          this.service.id = res.data.id;
          this.username = res.data.username;
        }
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 403) {
          this.router.navigate(['/login']);
        }
      },
    });
  }

  getCurrentUser(): Observable<any> {
    return this.http.get(`${this.apiUrl}/me`, { withCredentials: true });
  }
}
