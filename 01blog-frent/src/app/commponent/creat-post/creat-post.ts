import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CreatPostService } from '../../services/creat-post-service';



@Component({
  selector: 'app-creat-post',
  imports: [CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,MatIconModule],
  templateUrl: './creat-post.html',
  styleUrl: './creat-post.css',
})
export class CreatPost {
  postForm: FormGroup;
  previews: PreviewFile[] = [];

  constructor(private fb: FormBuilder , private snackBar: MatSnackBar , private creatPostService: CreatPostService) {
    this.postForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(100)]],
      content: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    });
  }

  onFileSelect(event: any) {
    let files: File[] = Array.from(event.target.files);

    if (files.length > 4) {
      this.snackBar.open('You can upload a maximum of 4 files.', 'Close', { duration: 3000  , panelClass: ['error-snackbar']});
      files = files.slice(0, 4);
    }

    this.previews = [];

    files.forEach((file) => {
      const reader = new FileReader();
      reader.onload = () => {
        this.previews.push({
          file,
          url: reader.result as string,
          type: file.type.startsWith('image') ? 'image' : 'video',
          isOld: false
        });
      };
      reader.readAsDataURL(file);
    });
  }

  submit() {
    if (this.postForm.invalid) {
      this.postForm.markAllAsTouched();
      return;
    }

    const formData = new FormData();

   
    this.previews.forEach((p) => {
      formData.append('image', p.file as Blob);
    });

    formData.append('title', this.postForm.value.title);
    formData.append('content', this.postForm.value.content);
    this.creatPostService.savepost(formData).subscribe({
      next: (res) => {
        this.snackBar.open('Post created successfully!', 'Close', { duration: 3000 , panelClass: ['success-snackbar']});
        this.postForm.reset();
        this.previews = [];
      }
    });
   
  }


}

