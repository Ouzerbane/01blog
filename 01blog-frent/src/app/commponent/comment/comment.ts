
import { Component, EventEmitter, Input, Output, signal, Signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { oneComment, onePost } from '../../modules/home/home-module';
import { CommonModule } from '@angular/common';
import { PostActions } from '../../services/post-actions';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Confirm } from '../confirm/confirm';

@Component({
  selector: 'app-comment',
  imports: [MatCardModule, MatDividerModule, CommonModule, MatIconModule, Confirm],
  templateUrl: './comment.html',
  styleUrl: './comment.css',
})
export class Comment {
  @Input() comment!: oneComment;
  @Input() post!: onePost;

  @Output() commentDeleted = new EventEmitter<string>();
  showConfirm = signal(false);

  constructor(private postAction: PostActions, private snackBar: MatSnackBar, private router: Router) { }

  deleteComment(commentt: oneComment) {
    this.postAction.deleteComment(commentt.postId, commentt.id).subscribe({
      next: () => {
        this.snackBar.open('Comment deleted successfully.', 'Close', { duration: 3000, panelClass: ['success-snackbar'] });
        this.commentDeleted.emit(commentt.id);
      }
    });
  }
  hasComment(): boolean {
    return !!this.comment && Object.keys(this.comment).length > 0;
  }

  openConfirm() {
    this.showConfirm.set(true);
  }

  confirmDelete(action: boolean) {
    if (action) {
      this.deleteComment(this.comment);
    }
    this.showConfirm.set(false);
  }

  closeConfirmDialog() {
    this.showConfirm.set(false);
  }

  getProfile(id: string) {
    this.router.navigate(["/profile", id]);
  }

}