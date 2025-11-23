import { Component, OnInit } from '@angular/core';
import { UserAdminDto } from './admin.model';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin',
  imports: [HttpClientModule, FormsModule, CommonModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin implements OnInit {
  users: UserAdminDto[] = [];


  constructor(
    private http: HttpClient,
  ) { }

  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    this.getUsers()
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
            imageUrl: user.imageUrl
              ? `http://localhost:8080/post${user.imageUrl}`
              : null,
          }));
        },
        error: (err) => {
          console.error('Error fetching suggested users', err);
        },
      });
  }

  DeletUser(userId: number) {
    const url = `http://localhost:8080/admin/remove-user`;

    const options = {
      body: { id: userId },
      withCredentials: true // quest
    };

    this.http.delete(url, options).subscribe({
      next: (data) => {
        // this.posts = this.posts.filter(p => p.id !== post.id);
        console.log('✅ Post deleted successfully',data);
      },
      error: (err) => {
        console.error('Error deleting post', err)

      },
    });
  }

    BanUser(userId: number) {
    const url = `http://localhost:8080/admin/ban-user`;

    const options = {
      body: { id: userId },
      withCredentials: true // quest
    };

    this.http.put(url, options).subscribe({
      next: (data) => {
        // this.posts = this.posts.filter(p => p.id !== post.id);
        console.log('✅ Post deleted successfully',data);
      },
      error: (err) => {
        console.error('Error deleting post', err)

      },
    });
  }
}
