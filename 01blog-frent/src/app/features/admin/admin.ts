import { Component } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { PostsAdminDto, ReportAdminDto, UserAdminDto, Media } from '../../modules/admin/admin-module';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin-service';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmProfile } from '../../commponent/confirm-profile/confirm-profile';


@Component({
  selector: 'app-admin',
  imports: [MatTableModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule, CommonModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin {
  users: UserAdminDto[] = [];

  reports: ReportAdminDto[] = [];

  posts: PostsAdminDto[] = [];



  constructor(
    private http: HttpClient,
    // private route: ActivatedRoute,
    private adminService: AdminService,
    private router: Router,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    this.getUsers()
    this.getReports()
    this.getPost()
  }

  getPost() {
    this.adminService.getPostsService().subscribe({
      next: (res) => {
        const data = res.data;
        this.posts = data.map((post: PostsAdminDto) => ({
          ...post,
          media: post.media ? post.media.map((media: Media) => ({
            ...media,
            url: `http://localhost:8080/post${media.url}`
          })) : null,
          author: {
            ...post.author,
            imageUrl: post.author.imageUrl ? `http://localhost:8080/post${post.author.imageUrl}` : null
          },
          status: post.status == "show" ? "Hide" : "show"
        }));

      }
    });
  }


  cancelRemoveProfile(ReportAdmin: ReportAdminDto) {
    ReportAdmin.showConfirm = false;
  }

  openRemoveProfile(ReportAdmin: ReportAdminDto) {
    ReportAdmin.showConfirm = true;
  }

  activRemoveProfile(ReportAdmin: ReportAdminDto) {
    this.DeletReport(ReportAdmin.id);
  }






  showpopap(post: any) {
    post.showConfirm = !post.showConfirm;
  }

  showConfirmHid(post: any) {
    post.showConfirmHid = !post.showConfirmHid;
  }

  confirmHid(post: any) {
    this.changeStatus(post.id)
    this.cancelHid(post)
  }
  cancelHid(post: any) {
    post.showConfirmHid = false
  }


  showConfirmBan(user: any) {
    user.showConfirmBan = !user.showConfirmBan;
  }

  confirmBan(user: any) {
    this.BanUser(user.id)
    this.cancelBan(user)
  }
  cancelBan(user: any) {
    user.showConfirmBan = false
  }




  getUsers() {
    this.adminService.getUsersService().subscribe({
      next: (res) => {
        const data = res.data;
        this.users = data.map((user: UserAdminDto) => ({
          ...user,
          imageUrl: user.imageUrl ? `http://localhost:8080/post${user.imageUrl}` : null,
          ban: user.action == "BAN" ? "Debane" : "Ban"
        }));
      }
    });
  }


  getReports() {
    this.adminService.getReportsService().subscribe({
      next: (res) => {
        const data = res.data;
        this.reports = data;
      }
    });
  }

  DeletReport(userId: string) {
    this.adminService.DeletReportService(userId).subscribe({
      next: (data) => {
        this.snackBar.open('Report deleted successfully.', 'Close', { duration: 3000, panelClass: ['success-snackbar'] });
        this.reports = this.reports.filter((rep) => rep.id != userId)
      },
    });
  }


  DeletUser(userId: string) {
    this.adminService.DeletUserService(userId).subscribe({
      next: (data) => {
        this.snackBar.open('User deleted successfully.', 'Close', { duration: 3000, panelClass: ['success-snackbar'] });
        this.users = this.users.filter((usr) => usr.id != userId)
        this.reports = this.reports.filter((rep) => { rep.targetUserId != userId && rep.reporterId != userId })
        this.posts = this.posts.filter((pst) => pst.author.id != userId)
      },
    });
  }

  BanUser(userId: string) {

    this.adminService.BanUserService(userId).subscribe({
      next: (data) => {
        this.snackBar.open('User banned successfully.', 'Close', { duration: 3000, panelClass: ['success-snackbar'] });
        this.users = this.users.map((usr) => {
          if (usr.id == userId) {
            usr.action = usr.action == "BAN" ? "ACTIVE" : "BAN"
            usr.ban = usr.ban == "Debane" ? "Ban" : "Debane"
          }
          return usr
        })
      },
    });

  }

  changeStatus(userId: string) {
    this.adminService.ChangeStatusService(userId).subscribe({
      next: (data) => {
        this.snackBar.open('Post status changed successfully.', 'Close', { duration: 3000, panelClass: ['success-snackbar'] });
        this.posts = this.posts.map((post) => {
          if (post.id == userId) {
            post.status = post.status == "show" ? "Hide" : "show"
          }
          return post
        })
      },
    });
  }


  deletePost(post: any) {
    this.adminService.DeletePostService(post.id).subscribe({
      next: (data) => {
        this.snackBar.open('Post deleted successfully.', 'Close', { duration: 3000, panelClass: ['success-snackbar'] });
        this.posts = this.posts.filter((pst) => pst.id != post.id)
      },
    });
  }






}
