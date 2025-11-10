import { Component, OnInit } from '@angular/core';
import { Post, PostsResponse } from '../dashboard/dashboard.model';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';


export interface UserDto {
  username: string;
  email: string;
  imagUrl: string;
  followers: number;
  following: number;
}

@Component({
  selector: 'app-profile',
  imports: [CommonModule, HttpClientModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  posts: Post[] = [];
  userId = 0;
  user?: UserDto;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    this.getUser(this.userId);
    this.getPosts(this.userId);
  }

  getPosts(id: number) {
    const postsUrl = `http://localhost:8080/get-post-profile/${id}`;
    this.http.get<PostsResponse>(postsUrl, { withCredentials: true }).subscribe({
      next: (res) => {
        this.posts = res.data;
        console.log('Posts:', this.posts);
      },
      error: (err) => console.error('Error fetching posts', err),
    });
  }

  getUser(id: number) {
    const userUrl = `http://localhost:8080/get-userInfo/${id}`;
    this.http.get<any>(userUrl, { withCredentials: true }).subscribe({
      next: (res) => {
        this.user = res.data;
        console.log('User info:', res);
      },
      error: (err) => console.error('Error fetching user', err),
    });
  }

  editProfile() {
    console.log('Edit profile clicked!');
  }
}