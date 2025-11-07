import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [HttpClientModule, FormsModule, CommonModule, DatePipe],
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

  // 🧩 Lists for modal
  followersList: Suggested[] = [];
  followingList: Suggested[] = [];
  showFollowers = false;
  showFollowing = false;

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
    const postsUrl = `http://localhost:8080/post/get-posts?page=${this.page}&size=${this.size}`;
    this.http.get<PostsResponse>(postsUrl, { withCredentials: true }).subscribe({
      next: (res) => (this.posts = res.data),
      error: (err) => console.error('Error fetching posts', err),
    });

    if (!this.areIsFirstTime) {
      this.areIsFirstTime = true;
      this.getSuggested();
      this.getFollowCounts();
    }
  }

  getSuggested() {
    const suggestedUrl = `http://localhost:8080/suggested`;
    this.http
      .get<SuggestedResponse>(suggestedUrl, { withCredentials: true })
      .subscribe({
        next: (res) => (this.suggested = res.data),
        error: (err) => console.error('Error fetching suggested users', err),
      });
  }

  getFollowCounts() {
    const followUrl = `http://localhost:8080/follow-counts`;
    this.http
      .get<FollowResponse>(followUrl, { withCredentials: true })
      .subscribe({
        next: (res) => {
          this.followers = res.data.followers;
          this.following = res.data.following;
        },
        error: (err) => console.error('Error fetching follow counts', err),
      });
  }

  toggleFollow(user: Suggested) {
    user.followed = !user.followed;
    const followUrl = `http://localhost:8080/follow`;
    this.http
      .post<FollowuserResponse>(
        followUrl,
        { id: user.id },
        { withCredentials: true }
      )
      .subscribe({
        next: () => this.getFollowCounts(),
        error: (err) => console.error('Error toggling follow', err),
      });
  }

  getFollowers() {
    const url = `http://localhost:8080/get-Followers`;
    this.http
      .get<SuggestedResponse>(url, { withCredentials: true })
      .subscribe({
        next: (res) => {
          this.followersList = res.data;
          this.showFollowers = true;
          this.showFollowing = false;
        },
        error: (err) => console.error('Error fetching followers', err),
      });
  }

  getFollowing() {
    const url = `http://localhost:8080/get-Following`;
    this.http
      .get<SuggestedResponse>(url, { withCredentials: true })
      .subscribe({
        next: (res) => {
          this.followingList = res.data;
          this.showFollowing = true;
          this.showFollowers = false;
        },
        error: (err) => console.error('Error fetching following', err),
      });
  }

  changePage(newPage: number) {
    if (newPage < 0) return;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: newPage },
      queryParamsHandling: 'merge',
    });
  }

  closeModal() {
    this.showFollowers = false;
    this.showFollowing = false;
  }

  openFollowers() {
    this.getFollowers();
    this.showFollowers = true;
  }

  openFollowing() {
    this.getFollowing();
    this.showFollowing = true;
  }
}
