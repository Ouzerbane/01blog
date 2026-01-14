import { Component, EventEmitter, Input, OnInit, Output, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { oneComment, onePost } from '../../modules/home/home-module';
import { PostActions } from '../../services/post-actions';
import { Comment } from '../comment/comment';
import { MatFormField, MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatMenu, MatMenuTrigger } from "@angular/material/menu";
import { ViewChild, TemplateRef } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Confirm } from '../confirm/confirm';
import { Router } from '@angular/router';
import { EditPost } from '../edit-post/edit-post';
import { Report } from '../report/report';


@Component({
  selector: 'app-post',
  imports: [CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatChipsModule, Comment, MatFormField,
    MatFormFieldModule, MatInputModule,
    MatMenu, MatMenuTrigger, MatDialogModule, Confirm, EditPost,Report],
  templateUrl: './post.html',
  styleUrl: './post.css',
})
export class Post {
  @Input() post!: onePost;
  showComments = signal(false);
  newComment = signal('');
  showConfirm = signal(false);
  showEdit = signal(false);

  @Output() postDeleted = new EventEmitter<string>();
  showReportModal = signal(false);


  // @ViewChild('confirmDialog') confirmDialog!: TemplateRef<any>;


  constructor(private postActions: PostActions, private snackBar: MatSnackBar, private router: Router) { }

  openConfirm() {
    this.showConfirm.set(true);
  }

  confirmDelete(action: boolean) {
    if (action) {
      this.deletePost(this.post.id);
    }
    this.showConfirm.set(false);
  }

  closeConfirmDialog() {
    this.showConfirm.set(false);
  }

  editPost(arg0: onePost) {
    throw new Error('Method not implemented.');
  }

  deletePost(arg0: string) {
    this.postActions.deletePost(arg0).subscribe({
      next: (response) => {
        this.snackBar.open('Post deleted successfully.', 'Close', { duration: 3000, panelClass: ['success-snackbar'] });
        this.postDeleted.emit(arg0);
      }
    });

  }
  likePost(likePost: onePost) {
    this.postActions.likePost(likePost.id).subscribe({
      next: (response) => {
        likePost.countLike = response.data.count;
        likePost.like = response.data.like;
      }
    });
  }

  toggleComments() {
    this.showComments.set(!this.showComments());
    if (this.showComments()) {
      this.getCommentsCount(this.post);
    }
  }

  getCommentsCount(post: onePost) {
    this.postActions.getComments(post.id).subscribe({
      next: (response) => {
        console.log(response);

        post.comment = response.data.map((comment: oneComment) => ({
          ...comment,
          url: comment.url ? `http://localhost:8080/post${comment.url}` : null
        }));
      }

    });
  }

  addComment(content: string, id: string, post: onePost) {
    if (!content || content.trim() === '') {
      this.snackBar.open('Comment cannot be empty.', 'Close', { duration: 3000, panelClass: ['error-snackbar'] });
      return;
    }
    this.postActions.addComment(content, id).subscribe({
      next: (response) => {

        this.newComment.set('');
        response.data.url = response.data.url ? `http://localhost:8080/post${response.data.url}` : null;
        post.comment = [
          response.data,
          ...(post.comment ?? [])
        ];

        post.countCommets++;
      this.snackBar.open('Comment added successfully.', 'Close', { duration: 3000, panelClass: ['success-snackbar'] });

      }
    });
  }

  onCommentDeleted(commentId: string) {
    this.post.comment = this.post.comment.filter(
      c => c.id !== commentId
    );

    this.post.countCommets--;
  }

  getProfile(id: string) {
    this.router.navigate(["/profile", id]);
  }

  openEdit() {
    this.showEdit.set(true);
  }

  closeEdit() {
    this.showEdit.set(false);
  }

  onSaveEdit(event: { title: string; content: string, files: PreviewFile[] }) {
    const formData = new FormData();

    formData.append('title', event.title);
    formData.append('content', event.content);

    event.files
      .filter(f => !f.isOld && f.file)
      .forEach(file => {
        formData.append('image', file.file as File);
      });

    const oldMediaIds = event.files
      .filter(f => f.isOld)
      .map(f => f.id);

    formData.append('oldMediaIds', JSON.stringify(oldMediaIds));
    formData.append('id', this.post.id);

    this.postActions.editPost(formData).subscribe({
      next: (response) => {
        console.log("response", response);

        this.post = {
          ...this.post,
          title: response.data.title,
          content: response.data.content,
          media: response.data.media.map((mediaItem: any) => ({
            ...mediaItem,
            type: mediaItem.mediaType,
            url: `http://localhost:8080/post${mediaItem.mediaUrl}`
          }))
        };
        this.snackBar.open('Post edited successfully.', 'Close', { duration: 3000, panelClass: ['success-snackbar'] });
      }
    });
    // console.log(event);
    this.closeEdit();
  }

    RepoprtPost(reportData: { message: string; confirmed: boolean }) {
    if (reportData.confirmed) {
      this.postActions.reportPost(this.post.id, reportData.message).subscribe({
        next: (response) => {
          this.snackBar.open('Post reported successfully.', 'Close', { duration: 3000 });
        }
      });
    }
    this.showReportModal.set(false);
  }

  openReportModal() {
    this.showReportModal.set(!this.showReportModal());
  }
}