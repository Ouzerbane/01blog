import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FollowResponse, FollowuserResponse, NotificationModul, Post, PostsResponse, Suggested, SuggestedResponse } from './dashboard.model';
import { PostService } from '../services/post';

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
  size: number = 10;

  id: number = 0;

  // comments: Comment[] = [];
  newComment: string = "";
  // like?: Like;

  // followers: number = 0;
  // following: number = 0;
  areIsFirstTime: boolean = false;
  suggested: Suggested[] = [];


  notifications: NotificationModul[] = [];

  // Lists for modal
  // followersList: Suggested[] = [];
  // followingList: Suggested[] = [];
  // showFollowers = false;
  // showFollowing = false;
  showPopatPost = false;

  // showNotifacation = false;
  // notificationCount: number = 0;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    public service: PostService
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.page = +params['page'] || 0;
      this.getPosts();
    });
  }

  onImageSelected(event: any, post: any) {
    const file = event.target.files[0];
    if (!file) return;

    post.newImage = file;

    const reader = new FileReader();
    reader.onload = (e: any) => {
      post.previewImage = e.target.result;
    };
    reader.readAsDataURL(file);
  }


  // toggleNotification() {
  //   this.showNotifacation = !this.showNotifacation
  //   if (this.showNotifacation) {
  //     this.openNotification();
  //   }
  // }

  // openNotification() {
  //   const url = `http://localhost:8080/notifications`; // adjust endpoint if needed
  //   this.http.get<any>(url, { withCredentials: true }).subscribe({
  //     next: (res) => {
  //       this.notifications = res.data; // assign to your local notifications array
  //       // this.getSuggested();
  //       console.log('Notifications loaded:', this.notifications);
  //     },
  //     error: (err) => {
  //       console.error('Error fetching notifications', err);
  //     }
  //   });
  // }

  getPosts() {
    const postsUrl = `http://localhost:8080/post/get-posts?page=${this.page}&size=${this.size}`;
    this.http.get<PostsResponse>(postsUrl, { withCredentials: true }).subscribe({
      next: (res) => {
        this.posts = res.data.map(post => ({
          ...post,
          imageUrl: post.imageUrl ? `http://localhost:8080/post${post.imageUrl}` : null
        }));
      },
      error: (err) => {
        // console.error('Error deleting post', err)

      },
    });

    if (!this.areIsFirstTime) {
      this.areIsFirstTime = true;
      this.getSuggested();
      // this.getFollowCounts();
    }
  }



  toggleComments(post: Post) {
    post.showComment = !post.showComment;

    if (post.showComment) {
      this.getComment(post);
    }
  }

  getProfile(id: number) {
    this.router.navigate(["/profile", id]);
  }

  getComment(post: Post) {
    const url = `http://localhost:8080/post/get-comments?postId=${post.id}`;
    this.http.get<any>(url, { withCredentials: true }).subscribe({
      next: (res) => {
        post.comment = res.data;
        console.log('Comments for post', post.id, post.comment);
      },
      error: (err) => {
        // console.error('Error deleting post', err)

      },
    });
  }


  addComment(post: Post) {
    if (!post.newComment || post.newComment.trim() === '') return;

    const payload = {
      id: post.id,
      content: post.newComment
    };

    this.http.post<any>(`http://localhost:8080/post/add-comments`, payload, { withCredentials: true })
      .subscribe({
        next: (res) => {
          post.comment.push(res.data);
          post.countCommets++;
          post.newComment = '';
        },
        error: (err) => {
          // console.error('Error deleting post', err)

        },
      });
  }




  getSuggested() {
    const suggestedUrl = `http://localhost:8080/suggested`;
    this.http
      .get<SuggestedResponse>(suggestedUrl, { withCredentials: true })
      .subscribe({
        next: (res) => {
          const data = res.data;

          this.suggested = data.map(user => ({
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


  // getFollowCounts() {
  //   const followUrl = `http://localhost:8080/follow-counts`;
  //   this.http
  //     .get<FollowResponse>(followUrl, { withCredentials: true })
  //     .subscribe({
  //       next: (res) => {
  //         this.followers = res.data.followers;
  //         this.following = res.data.following;
  //         // this.notificationCount = res.data.notification;
  //       },
  //       error: (err) => {
  //         // console.error('Error deleting post', err)

  //       },
  //     });
  // }

  @HostListener('document:click', ['$event'])
closeMenus(event: Event) {
  this.posts.forEach(post => {
    const clickedInside = (event.target as HTMLElement).closest('.post-menu');
    if (!clickedInside) {
      post.showMenu = false;
    }
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
        next: () => {
          // this.getFollowCounts()

        },
        error: (err) => {
          // console.error('Error deleting post', err)

        },
      });
  }

  // getFollowers() {
  //   const url = `http://localhost:8080/get-Followers/${this.service.id}`;

  //   this.http
  //     .get<SuggestedResponse>(url, { withCredentials: true })
  //     .subscribe({
  //       next: (res) => {
  //         this.followersList = res.data.map(user => ({
  //           ...user,
  //           imageUrl: user.imageUrl ? `http://localhost:8080/post${user.imageUrl}` : null
  //         }));
  //         this.showFollowers = true;
  //         this.showFollowing = false;
  //       },
  //       error: (err) => {
  //         // console.error('Error deleting post', err)

  //       },
  //     });

  // }

  // getFollowing() {
  //   const url = `http://localhost:8080/get-Following/${this.service.id}`;
  //   this.http
  //     .get<SuggestedResponse>(url, { withCredentials: true })
  //     .subscribe({
  //       next: (res) => {
  //         this.followingList = res.data.map(user => ({
  //           ...user,
  //           imageUrl: user.imageUrl ? `http://localhost:8080/post${user.imageUrl}` : null
  //         }));
  //         this.showFollowing = true;
  //         this.showFollowers = false;
  //       },
  //       error: (err) => {
  //         // console.error('Error deleting post', err)

  //       },
  //     });
  // }

  changePage(newPage: number) {
    if (newPage < 0) return;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: newPage },
      queryParamsHandling: 'merge',
    });
  }

  // closeModal() {
  //   this.showFollowers = false;
  //   this.showFollowing = false;
  // }

  // openFollowers() {
  //   this.getFollowers();
  //   this.showFollowers = true;
  // }

  // openFollowing() {
  //   this.getFollowing();
  //   this.showFollowing = true;
  // }

  PopatPost() {
    this.router.navigate(['/post'])
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
    const url = `http://localhost:8080/post/edit-post`;

    const formData = new FormData();
    formData.append("id", post.id);
    formData.append("title", post.editTitle);
    formData.append("content", post.editContent);

    if (post.newImage) {
      formData.append("image", post.newImage);
    } else {
      formData.append("imageUrl", post.imageUrl);
    }

    this.http.put(url, formData, { withCredentials: true }).subscribe({
      next: (res: any) => {
        post.title = post.editTitle;
        post.content = post.editContent;

        if (post.previewImage) post.imageUrl = post.previewImage;

        post.isEditing = false;
      },
      error: (err) => console.error("Error updating post", err)
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
        // console.error('Error deleting post', err)

      },
    });
  }


  likePost(post: any) {
    const url = `http://localhost:8080/post/like-post`;
    const body = { id: post.id };

    this.http.post<any>(url, body, { withCredentials: true }).subscribe({
      next: (res) => {
        post.countLike = res.data.count;
        post.like = res.data.like;

        console.log(`👍 Like updated: ${post.liked}, total: ${post.likesCount}`);
      },
      error: (err) => {
        // console.error('Error deleting post', err)

      },
    });
  }

}
