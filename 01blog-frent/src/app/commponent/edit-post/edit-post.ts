import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-edit-post',
  imports: [CommonModule,
    FormsModule,  // Changed from ReactiveFormsModule
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule],
  templateUrl: './edit-post.html',
  styleUrl: './edit-post.css',
})
export class EditPost {
  @Input() postTitle: string = '';
  @Input() postContent: string = '';
  @Input() currentMedia: { id: string; url: string; type: string }[] | null = [];

  prevewes: PreviewFile[] = [];

  @Output() cancelEditEvent = new EventEmitter<void>();
  @Output() saveEditEvent = new EventEmitter<{ title: string; content: string, files: PreviewFile[] }>();

  constructor(private snackBar: MatSnackBar) { }

  onFileSelect($event: Event) {
    let files: File[] = Array.from(($event.target as HTMLInputElement).files || []);

    if (files.length + (this.currentMedia?.length ?? 0) > 4) {
      this.snackBar.open('You can upload a maximum of 4 files.', 'Close', { duration: 3000 });
      files = files.slice(0, 4 - (this.currentMedia?.length ?? 0));
    }

    this.prevewes = [];

    if (files.length === 0) {
      return;
    }
    this.prevewes = [];
    files.forEach((file) => {
      const reader = new FileReader();
      reader.onload = () => {
        this.prevewes.push({
          file,
          url: reader.result as string,
          type: file.type.startsWith('image') ? 'image' : 'video',
          isOld: false
        });
      };
      reader.readAsDataURL(file);
    });

  }
  // emit الأحداث
  cancelEdit() {
    this.cancelEditEvent.emit();
  }

  saveEdit() {
    if (this.postTitle.trim() == '' || this.postTitle.trim().length == 0 || this.postTitle.trim().length < 100) {
      this.snackBar.open('title most between 1 and 100', 'Close', { duration: 3000 });
    }

    if (this.postContent.trim() == '' || this.postContent.trim().length == 0 || this.postContent.trim().length < 501) {
      this.snackBar.open('content most between 1 and 500', 'Close', { duration: 3000 });
    }
    this.joinOldAndNewMedia();
    this.saveEditEvent.emit({
      title: this.postTitle.trim(),
      content: this.postContent.trim(),
      files: this.prevewes
    });
  }

  removeNewMedia(id: string) {
    this.currentMedia = this.currentMedia?.filter(media => media.id !== id) || null;
  }

  removePreview(name: string) {
    this.prevewes = this.prevewes.filter(preview => preview.file?.name !== name);
  }
  joinOldAndNewMedia() {
    const oldMedia: PreviewFile[] =
      (this.currentMedia ?? []).map(media => ({
        id: media.id,
        url: media.url,
        type: media.type === 'IMAGE' ? 'image' : 'video',
        isOld: true
      }));

    this.prevewes = [...oldMedia, ...this.prevewes];
  }


}
