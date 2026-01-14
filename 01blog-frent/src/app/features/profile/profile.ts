import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { onePost, Suggested } from '../../modules/home/home-module';
import { UserDto } from '../../modules/profile/profile-module';
import { profileService } from '../../services/profileService';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalInfo } from '../../services/global-info';
import { Post } from '../../commponent/post/post';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Report } from '../../commponent/report/report';

@Component({
  selector: 'app-profile',
  imports: [CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule, Post, Report],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {



  followersList = signal<Suggested[]>([]);
  showFollow = signal(false);
  typeFollow = signal('');
  // foolowingList = signal<Suggested[]>([]);

  postsList = signal<onePost[]>([]);
  file?: File;
  user = signal<UserDto>({} as UserDto);

  localUserid = signal("");

  userNotFund = signal(false);

  showReportModal = signal(false);

  userId: string = '';

  constructor(private profileService: profileService, private router: Router,
    private route: ActivatedRoute, public globalInfo: GlobalInfo, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const userId = params.get('id');
      if (userId) {
        this.userId = userId;
      } else {
        this.userId = this.globalInfo.idUser;
      }
      this.localUserid.set(this.globalInfo.idUser);

      this.getUserProfileById(this.userId);
      this.getPosts();
    });
  }

  getUserProfileById(id: string) {
    this.profileService.grtUserprofile(id).subscribe({
      next: (req) => {
        this.user.set({
          ...req.data,
          imageUrl: req.data.imagUrl ? `http://localhost:8080/post${req.data.imagUrl}` : undefined
        });
        this.fk()

        // if (this.user().id != undefined) {
        //   this.userNotFund.set(true);
        // }
        console.log(this.user(), "oooooooooooo", this.user().email);


      }
    });
  }

  fk() {
    console.log(this.user().email);

    if (this.user().email != undefined) {
      this.userNotFund.set(true);
    } else {
      this.userNotFund.set(false);
    }
  }

  getPosts() {
    this.profileService.getPostsProfile(this.userId).subscribe({
      next: (req) => {
        console.log(req);

        this.postsList.set(req.data.map(pst => ({
          ...pst,
          media: pst.media ? pst.media.map(mediaItem => ({
            ...mediaItem,
            url: `http://localhost:8080/post${mediaItem.url}`
          })) : null,
          author: {
            ...pst.author,
            imagUrl: pst.author.imagUrl ? `http://localhost:8080/post${pst.author.imagUrl}` : null
          }
        })));
      }
    });
  }


  onDeletPost(postId: string) {
    this.postsList.set(this.postsList().filter(post => post.id !== postId));
  }

  RepoprtUser(reportData: { message: string; confirmed: boolean }) {
    if (reportData.confirmed) {
      this.profileService.reportUser(reportData.message, this.userId).subscribe({
        next: (response) => {
          this.snackBar.open('User reported successfully.', 'Close', { duration: 3000 });
        }
      });
    }
    this.showReportModal.set(false);
  }

  openReportModal() {
    this.showReportModal.set(!this.showReportModal());
  }

  onFileSelectProfile($event: Event) {
    const file: File | null = ($event.target as HTMLInputElement).files?.[0] || null;
    if (file) {
      this.file = file;
      const formData = new FormData();
      formData.append('image', this.file);
      this.profileService.uploadProfilePicture(formData).subscribe({
        next: (res) => {
          this.snackBar.open('Profile picture updated successfully.', 'Close', { duration: 3000 });
          // this.getUserProfileById(this.userId);
          this.user.update(user => ({
            ...user,
            imageUrl: `http://localhost:8080/post${res.data}`
          }));
        }
      });
    }

  }


  openFollowers(type: string) {
    this.typeFollow.set(type);
    this.showFollow.set(true);
    if (type === 'Following') {
      this.getFollowingList();
      return;
    } else {
      this.getFollowersList();
    }
  }
  closeFollowers() {
    this.showFollow.set(false);
  }

  getFollowersList() {
    this.profileService.getFollowers(this.userId).subscribe({
      next: (res) => {
        console.log("followerslist", res);

        this.followersList.set(res.data.map(follower => ({
          ...follower,
          imageUrl: follower.imageUrl ? `http://localhost:8080/post${follower.imageUrl}` : undefined
        })));
      }
    });
  }
  getFollowingList() {
    this.profileService.getFollowing(this.userId).subscribe({
      next: (res) => {
        console.log("followinglist", res);

        this.followersList.set(res.data.map(following => ({
          ...following,
          imageUrl: following.imageUrl ? `http://localhost:8080/post${following.imageUrl}` : undefined
        })));
      }
    });
  }
  getProfile(arg0: string) {
    throw new Error('Method not implemented.');
  }
  toggleFollow(id: string) {
    this.profileService.toggleFollowUser(id).subscribe({
      next: (res) => {
        console.log(res);
        this.followersList.set(this.followersList().map(user => {
          if (user.id === id) {
            return {
              ...user,
              followed: res.data.followed
            };
          }
          return user;
        }));
      }
    });
  }
}
