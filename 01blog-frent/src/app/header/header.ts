import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostService } from '../services/post';
import { NotificationModul } from '../dashboard/dashboard.model';

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

  userType: boolean = false;

  showNotifacation = false;
  notificationCount: number = 0;
  notifications: NotificationModul[] = [];

  showProfileMenu = false;

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
    this.CountNotification()
    this.GetuserType()
  }



  toggleProfileMenu() {
    this.showProfileMenu = !this.showProfileMenu;
  }

  logout() {
    const url = 'http://localhost:8080/logout';

    this.http.post(url, {}, { withCredentials: true }).subscribe({
      next: (req) => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.log(err);

      }
    })
  }



  toggleNotification() {
    this.showNotifacation = !this.showNotifacation
    if (this.showNotifacation) {
      this.openNotification();
    }

  }

  openNotification() {
    const url = `http://localhost:8080/notifications`; 
    this.http.get<any>(url, { withCredentials: true }).subscribe({
      next: (res) => {
        this.notifications = res.data; 
        this.CountNotification()
      },
      error: (err) => {
        console.error('Error fetching notifications', err);
      }
    });
  }




  CountNotification() {
    const url = `http://localhost:8080/count-notifications`; 
    this.http.get<any>(url, { withCredentials: true }).subscribe({
      next: (res) => {
        this.notificationCount = res.data; 
      },
      error: (err) => {
        console.error('Error fetching notifications', err);
      }
    });
  }



  GetuserType() {
    const url = `http://localhost:8080/admin/user-type`; 
    this.http.get<any>(url, { withCredentials: true }).subscribe({
      next: (res) => {
        this.userType = res.data;
        console.log('NNNNNNNNNNNNN', res.data);
      },
      error: (err) => {
        console.error('Error fetching notifications', err);
      }
    });
  }

  getCurrentUser(): Observable<any> {
    return this.http.get(`${this.apiUrl}/me`, { withCredentials: true });
  }
  
}
