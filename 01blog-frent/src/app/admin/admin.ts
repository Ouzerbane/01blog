import { Component, OnInit } from '@angular/core';
import { PostsAdminDto, ReportAdminDto, UserAdminDto } from './admin.model';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Action } from 'rxjs/internal/scheduler/Action';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin',
  imports: [HttpClientModule, FormsModule, CommonModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin implements OnInit {
  users: UserAdminDto[] = [];

  reports: ReportAdminDto[] = [];

  posts: PostsAdminDto[] = [];



  constructor(
    private http: HttpClient,
    // private route: ActivatedRoute,
    private router: Router,
  ) { }

  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    this.getUsers()
    this.getReports()
    this.getPost()
  }

  getPost() {
    const api = `http://localhost:8080/admin/posts`;
    this.http
      .get<any>(api, { withCredentials: true })
      .subscribe({
        next: (res) => {
          const data = res.data;
          console.log(data);

          this.posts = data.map((post: PostsAdminDto) => ({
            ...post,
            imageUrl: post.imageUrl ? `http://localhost:8080/post${post.imageUrl}` : null,
            status: post.status == "show" ? "Hide" : "show"
          }));
        },
        error: (err) => {
         this.handleError(err)
        },
      });
  }


  showpopap(post: any) {
    post.showConfirm = !post.showConfirm;
  }
  


  getUsers() {
    const api = `http://localhost:8080/admin/users`;
    this.http
      .get<any>(api, { withCredentials: true })
      .subscribe({
        next: (res) => {
          const data = res.data;
          console.log(data);

          this.users = data.map((user: UserAdminDto) => ({
            ...user,
            imageUrl: user.imageUrl ? `http://localhost:8080/post${user.imageUrl}` : null,
            ban: user.action == "BAN" ? "Debane" : "Ban"
          }));
        },
        error: (err) => {
         this.handleError(err) ;
        },
      });
  }


  getReports() {
    const api = `http://localhost:8080/admin/reports`;
    this.http
      .get<any>(api, { withCredentials: true })
      .subscribe({
        next: (res) => {
          const data = res.data;
          console.log(data);
          this.reports = data;


        },
        error: (err) => {
          this.handleError(err);
        },
      });
  }

  DeletReport(userId: number) {
    const url = `http://localhost:8080/admin/remove-report`;
    const options = {
      body: { id: userId },
      withCredentials: true
    };

    this.http.delete(url, options).subscribe({
      next: (data) => {
        console.log(' Post deleted successfully', data);
        this.reports = this.reports.filter((repot) => repot.id != userId)
      },
      error: (err) => {
        this.handleError(err)

      },
    });
  }


  DeletUser(userId: number) {
    const url = `http://localhost:8080/admin/remove-user`;

    const options = {
      body: { id: userId },
      withCredentials: true
    };

    this.http.delete(url, options).subscribe({
      next: (data) => {
        console.log(' Post deleted successfully', data);
        this.users = this.users.filter((usr) => usr.id != userId)
      },
      error: (err) => {
        this.handleError(err)

      },
    });
  }

  BanUser(userId: number) {
    const url = `http://localhost:8080/admin/BanUser`;

    const body = { id: userId };

    const options = {
      withCredentials: true
    };

    this.http.put(url, body, options).subscribe({
      next: (data) => {
        console.log('✅ User banned successfully', data);
        this.users = this.users.map((usr) => {
          if (usr.id == userId) {
            usr.action = usr.action == "BAN" ? "ACTIVE" : "BAN"
            usr.ban = usr.ban == "Debane" ? "Ban" : "Debane"
          }
          return usr
        })
      },
      error: (err) => {
        this.handleError(err);
      },
    });
  }

  changeStatus(userId: number) {
    const url = `http://localhost:8080/admin/changeStatus`;

    const body = { id: userId };

    const options = {
      withCredentials: true
    };

    this.http.put(url, body, options).subscribe({
      next: (data) => {
        console.log('User banned successfully', data);
        this.posts = this.posts.map((post) => {
          if (post.id == userId) {
            post.status = post.status == "show" ? "Hide" : "show"
          }
          return post
        })
      },
      error: (err) => {
        this.handleError(err);
      },
    });
  }


  deletePost(post: any) {
    const url = `http://localhost:8080/post/delete-post`;

    const options = {
      body: { id: post.id },
      withCredentials: true // quest
    };

    this.http.delete(url, options).subscribe({
      next: () => {
        this.posts = this.posts.filter(p => p.id !== post.id);
        console.log('✅ Post deleted successfully');
      },
      error: (err) => {
        this.handleError(err)

      },
    });
  }


    handleError(err: any) {
    if (err.error && err.error.errors && err.error.errors.length > 0) {
      const firstError = err.error.errors[0];

      if (firstError.field === 'token') {
        alert('Session expired. Please login again.');
        this.router.navigate(['/login']);
      } else {
        alert(firstError.message || 'Something went wrong');
      }
    } else {
      console.error('Unknown error', err);
      alert('An unexpected error occurred');
    }
  }

}
