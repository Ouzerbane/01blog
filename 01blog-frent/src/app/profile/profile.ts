import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { FollowuserResponse, Post, PostsResponse, Suggested, SuggestedResponse } from '../dashboard/dashboard.model';
import { FormsModule } from '@angular/forms';

export interface UserDto {
  id: number;
  username: string;
  email: string;
  imageUrl?: string;
  followers: number;
  following: number;
}


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, HttpClientModule, DatePipe, FormsModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css'],
})
export class Profile implements OnInit {
  posts: Post[] = [];
  userId = 0;
  user?: UserDto;

  // followers modal
  followersList: Suggested[] = [];
  followingList: Suggested[] = [];
  showFollowers = false;
  showFollowing = false;

  reportReason = "";
  showReportTemplite = false;

  //  private Long targetUserId;
  //   private String reason;

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    // this.userId = Number(this.route.snapshot.paramMap.get('id'));
    // this.getUser(this.userId);
    // this.getPosts(this.userId);
    this.route.paramMap.subscribe(params => {
      this.userId = Number(params.get('id'));
      this.getUser(this.userId);
      this.getPosts(this.userId);
    });
  }

  // Get user info
  getUser(id: number) {
    const url = `http://localhost:8080/get-userInfo/${id}`;
    this.http.get<any>(url, { withCredentials: true }).subscribe({
      next: (res) => (this.user = res.data),
      error: (err) => console.error('Error fetching user', err),
    });
  }

  report() {
    this.showReportTemplite = !this.showReportTemplite;
  }

  reportUser() {
    const body = { reason: this.reportReason, targetUserId: this.userId };

    this.http.post('http://localhost:8080/report-user', body, { withCredentials: true })
      .subscribe({
        next: () => {
          this.reportReason = '';
          this.showReportTemplite = false;
        },
        error: (err) => {
          console.error('Error sending report', err);
        }
      });
  }

  // Get user posts
  getPosts(id: number) {
    const url = `http://localhost:8080/get-post-profile/${id}`;
    this.http.get<PostsResponse>(url, { withCredentials: true }).subscribe({
      next: (res) => (this.posts = res.data),
      error: (err) => console.error('Error fetching posts', err),
    });
  }


  toggleFollow(user: Suggested) {
    user.followed = !user.followed;
    const followUrl = `http://localhost:8080/follow`;
    this.http
      .post<FollowuserResponse>(followUrl, { id: user.id }, { withCredentials: true })
      .subscribe({
        next: (resp) => { console.log(resp); this.getUser(this.userId) },
        error: (err) => console.error('Error toggling follow', err),
      });
  }

  //  Followers & Following
  openFollowers() {
    console.log("oppwnFollowers");

    const url = `http://localhost:8080/get-Followers/${this.userId}`;
    this.http.get<SuggestedResponse>(url, { withCredentials: true }).subscribe({
      next: (res) => {
        this.followersList = res.data;
        this.showFollowers = true;
        this.showFollowing = false;
      },
      error: (err) => console.error('Error fetching followers', err),
    });
  }

  getProfile(id: number) {
    this.router.navigate(["/profile", id]);

  }

  openFollowing() {
    const url = `http://localhost:8080/get-Following/${this.userId}`;
    this.http.get<SuggestedResponse>(url, { withCredentials: true }).subscribe({
      next: (res) => {
        this.followingList = res.data;
        this.showFollowing = true;
        this.showFollowers = false;
      },
      error: (err) => console.error('Error fetching following', err),
    });
  }

  closeModal() {
    this.showFollowers = false;
    this.showFollowing = false;
  }

  // 💬 Comments
  toggleComments(post: Post) {
    post.showComment = !post.showComment;
    if (post.showComment) this.getComments(post);
  }

  getComments(post: Post) {
    const url = `http://localhost:8080/post/get-comments?postId=${post.id}`;
    this.http.get<any>(url, { withCredentials: true }).subscribe({
      next: (res) => (post.comment = res.data),
      error: (err) => console.error('Error fetching comments', err),
    });
  }

  addComment(post: Post) {
    if (!post.newComment || post.newComment.trim() === '') return;

    const body = { id: post.id, content: post.newComment };
    this.http
      .post<any>(`http://localhost:8080/post/add-comments`, body, { withCredentials: true })
      .subscribe({
        next: (res) => {
          post.comment.push(res.data);
          post.countCommets++;
          post.newComment = '';
        },
        error: (err) => console.error('Error adding comment', err),
      });
  }

  //  Like Post
  likePost(post: any) {
    const url = `http://localhost:8080/post/like-post`;
    const body = { id: post.id };

    this.http.post<any>(url, body, { withCredentials: true }).subscribe({
      next: (res) => {
        post.countLike = res.data.count;
        post.like = res.data.like;
      },
      error: (err) => console.error('Error liking post', err),
    });
  }

  //Edit Post
  startEditing(post: any) {
    post.isEditing = true;
    post.editTitle = post.title;
    post.editContent = post.content;
  }

  cancelEditing(post: any) {
    post.isEditing = false;
  }

  savePost(post: any) {
    const url = `http://localhost:8080/post/edit-post`;
    const updatedPost = {
      id: post.id,
      title: post.editTitle,
      content: post.editContent,
      imageUrl: post.imageUrl,
    };

    this.http.put(url, updatedPost, { withCredentials: true }).subscribe({
      next: () => {
        post.title = post.editTitle;
        post.content = post.editContent;
        post.isEditing = false;
      },
      error: (err) => console.error('Error updating post', err),
    });
  }

  // 🗑️ Delete Post
  deletePost(post: any) {
    const url = `http://localhost:8080/post/delete-post`;
    const options = { body: { id: post.id }, withCredentials: true };

    this.http.delete(url, options).subscribe({
      next: () => {
        this.posts = this.posts.filter((p) => p.id !== post.id);
      },
      error: (err) => console.error('Error deleting post', err),
    });
  }
}
