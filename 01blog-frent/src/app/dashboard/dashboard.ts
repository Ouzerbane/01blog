import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

/* ================== Interfaces ================== */

/* ================== Component ================== */
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [HttpClientModule, FormsModule, CommonModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
})
export class Dashboard implements OnInit {
  posts: Post[] = [];
  page: number = 0;
  size: number = 5;

  followers: number = 0;
  following: number = 0;
  areIsFirstTime: boolean = false;
  suggested: Suggested[] = [];

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.page = +params['page'] || 0;
      this.getPosts();
    });
  }

  getPosts() {
    // 🧩 Fetch posts
    const postsUrl = `http://localhost:8080/post/get-posts?page=${this.page}&size=${this.size}`;
    this.http.get<PostsResponse>(postsUrl, { withCredentials: true }).subscribe({
      next: (res) => {
        this.posts = res.data;
      },
      error: (err) => console.error('Error fetching posts', err),
    });

    if (!this.areIsFirstTime) {
      this.areIsFirstTime = true;
      this.getSuggested();
      this.getfollow();
    }
  }

  getSuggested() {
    const suggestedUrl = `http://localhost:8080/suggested`;
    this.http.get<SuggestedResponse>(suggestedUrl, { withCredentials: true }).subscribe({
      next: (res) => {
        this.suggested = res.data;
        console.log("suggested data:", this.suggested);
      },
      error: (err) => console.error('Error fetching suggested users', err),
    });
  }

  getfollow() {
    const followUrl = `http://localhost:8080/follow-counts`;
    this.http.get<FollowResponse>(followUrl, { withCredentials: true }).subscribe({
      next: (res) => {
        this.followers = res.data.followers;
        this.following = res.data.following;
      },
      error: (err) => console.error('Error fetching follow counts', err),
    });
  }


  // 🟡 Change page without full reload
  changePage(newPage: number) {
    if (newPage < 0) return;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: newPage },
      queryParamsHandling: 'merge',
    });
  }

  // 🟢 Toggle follow/unfollow suggested user
  toggleFollow(user: Suggested) {
    user.followed = !user.followed;
    const followUrl = `http://localhost:8080/follow`;
    this.http.post<FollowuserResponse>(followUrl, { id: user.id }, { withCredentials: true }).subscribe({
      next: (res) => {
        user = res.data;
        this.getfollow();
      },
      error: (err) => console.error('Error fetching follow counts', err),
    });

  }


  followersList: Suggested[] = [];  // li ghadi tshow f modal
  followingList: Suggested[] = [];

  getFollowers() {
    const followUrl = `http://localhost:8080/get-Followers`;
    this.http.get<SuggestedResponse>(followUrl, { withCredentials: true }).subscribe({
      next: (res) => {
       this.followersList = res.data;
      },
      error: (err) => console.error('Error fetching follow counts', err),
    });
  }

  getFollowing() {
     const followUrl = `http://localhost:8080/get-Followers`;
    this.http.get<SuggestedResponse>(followUrl, { withCredentials: true }).subscribe({
      next: (res) => {
       this.followingList = res.data;
      },
      error: (err) => console.error('Error fetching follow counts', err),
    });
  }

}
