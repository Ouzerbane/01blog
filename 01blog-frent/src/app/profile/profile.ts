import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { FollowuserResponse, Post, PostsResponse, Suggested, SuggestedResponse } from '../dashboard/dashboard.model';
import { FormsModule } from '@angular/forms';
import { PostService } from '../services/post';

export interface UserDto {
  id: string;
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
  userId: string = ''; // UUID
  user?: UserDto;

  followersList: Suggested[] = [];
  followingList: Suggested[] = [];
  showFollowers = false;
  showFollowing = false;

  reportReason = "";
  showReportTemplite = false;

  isuser = false;

  @ViewChild('profileInput') profileInput!: any;

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router, public service: PostService) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (!idParam) return;
      this.userId = idParam; // UUID كـ string
      if (this.userId === this.service.id) {
        this.isuser = true;
      }
      this.getUser(this.userId);
      this.getPosts(this.userId);
    });
  }

  triggerImagePicker() {
    if (this.isuser) {
      this.profileInput.nativeElement.click();
    }
  }

  changeProfileImage(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("image", file);

    this.http.post<any>("http://localhost:8080/update-profile-image",formData,{ withCredentials: true }
    ).subscribe({
      next: (res) => {
        this.user!.imageUrl = res.data ? `http://localhost:8080/post${res.data}` : undefined;
      },
      error: (err) => this.handleError(err),
    });
  }

  showpopap(post: any) {
    post.showConfirm = !post.showConfirm;
  }

  onImageSelected(event: any, post: any) {
    const file = event.target.files[0];
    if (!file) return;
    post.newImage = file;
    const reader = new FileReader();
    reader.onload = (e: any) => post.previewImage = e.target.result;
    reader.readAsDataURL(file);
  }

  getUser(id: string) {
    const url = `http://localhost:8080/get-userInfo/${id}`;
    this.http.get<any>(url, { withCredentials: true }).subscribe({
      next: (res) => {
        console.log(res.data)
        const data = res.data;
        this.user = {
          ...data,
          imageUrl: data.imagUrl ? `http://localhost:8080/post${data.imagUrl}` : null,
        };
      },
      error: (err) => this.handleError(err),
    });
  }

  report() {
    this.showReportTemplite = !this.showReportTemplite;
  }

  reportUser() {
    const body = { reason: this.reportReason, targetUserId: this.userId };
    this.http.post('http://localhost:8080/report-user', body, { withCredentials: true }).subscribe({
      next: () => {
        this.reportReason = '';
        this.showReportTemplite = false;
      },
      error: (err) => this.handleError(err),
    });
  }

  getPosts(id: string) {
    const url = `http://localhost:8080/get-post-profile/${id}`;
    this.http.get<PostsResponse>(url, { withCredentials: true }).subscribe({
      next: (res) => {
        this.posts = res.data.map(post => ({
          ...post,
          imageUrl: post.imageUrl ? `http://localhost:8080/post${post.imageUrl}` : null,
        }));
      },
      error: (err) => this.handleError(err),
    });
  }

  toggleFollow(user: Suggested) {
    user.followed = !user.followed;
    this.http.post<FollowuserResponse>('http://localhost:8080/follow', { id: user.id }, { withCredentials: true })
      .subscribe({ next: () => this.getUser(this.userId), error: (err) => this.handleError(err) });
  }

  getProfile(id: string) {
    this.router.navigate(["/profile", id]);
  }

  openFollowers() {
    this.http.get<SuggestedResponse>(`http://localhost:8080/get-Followers/${this.userId}`, { withCredentials: true })
      .subscribe({
        next: (res) => {
          this.followersList = res.data.map(user => ({
            ...user,
            imageUrl: user.imageUrl ? `http://localhost:8080/post${user.imageUrl}` : undefined
          }));
          this.showFollowers = true;
          this.showFollowing = false;
        },
        error: (err) => this.handleError(err)
      });
  }

  openFollowing() {
    this.http.get<SuggestedResponse>(`http://localhost:8080/get-Following/${this.userId}`, { withCredentials: true })
      .subscribe({
        next: (res) => {
          this.followingList = res.data.map(user => ({
            ...user,
            imageUrl: user.imageUrl ? `http://localhost:8080/post${user.imageUrl}` : undefined
          }));
          this.showFollowing = true;
          this.showFollowers = false;
        },
        error: (err) => this.handleError(err)
      });
  }

  closeModal() {
    this.showFollowers = false;
    this.showFollowing = false;
  }

  toggleComments(post: Post) {
    post.showComment = !post.showComment;
    if (post.showComment) this.getComments(post);
  }

  getComments(post: Post) {
    const url = `http://localhost:8080/post/get-comments?postId=${post.id}`;
    this.http.get<any>(url, { withCredentials: true }).subscribe({
      next: (res) => post.comment = res.data,
      error: (err) => this.handleError(err),
    });
  }

  addComment(post: Post) {
    if (!post.newComment || post.newComment.trim() === '') return;
    const body = { id: post.id, content: post.newComment };
    this.http.post<any>(`http://localhost:8080/post/add-comments`, body, { withCredentials: true }).subscribe({
      next: (res) => {
        post.comment.push(res.data);
        post.countCommets++;
        post.newComment = '';
      },
      error: (err) => this.handleError(err),
    });
  }

  likePost(post: any) {
    const body = { id: post.id };
    this.http.post<any>(`http://localhost:8080/post/like-post`, body, { withCredentials: true }).subscribe({
      next: (res) => {
        post.countLike = res.data.count;
        post.like = res.data.like;
      },
      error: (err) => this.handleError(err),
    });
  }

  startEditing(post: any) {
    post.isEditing = true;
    post.editTitle = post.title;
    post.editContent = post.content;
  }

  cancelEditing(post: any) {
    post.isEditing = false;
  }

  savePost(post: any) {
    const formData = new FormData();
    formData.append("id", post.id);
    formData.append("title", post.editTitle);
    formData.append("content", post.editContent);
    if (post.newImage) formData.append("image", post.newImage);
    else formData.append("imageUrl", post.imageUrl);

    this.http.put(`http://localhost:8080/post/edit-post`, formData, { withCredentials: true }).subscribe({
      next: () => {
        post.title = post.editTitle;
        post.content = post.editContent;
        if (post.previewImage) post.imageUrl = post.previewImage;
        post.isEditing = false;
      },
      error: (err) => this.handleError(err)
    });
  }

  deletePost(post: any) {
    const options = { body: { id: post.id }, withCredentials: true };
    this.http.delete(`http://localhost:8080/post/delete-post`, options).subscribe({
      next: () => this.posts = this.posts.filter(p => p.id !== post.id),
      error: (err) => this.handleError(err)
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
