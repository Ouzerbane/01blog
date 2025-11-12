import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './posts.html',
  styleUrl: './posts.css',
})
export class Posts {
  title = '';
  content = '';
  imageUrl = ''; // not used directly anymore (will come from backend)
  err = '';
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;

  constructor(private http: HttpClient) {}

  // 🔹 Handle image selection + preview
  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;

      const reader = new FileReader();
      reader.onload = () => (this.imagePreview = reader.result);
      reader.readAsDataURL(file);
    }
  }

  // 🔹 Submit post
  onsubmet() {
    if (!this.title || !this.content) {
      this.err = 'Please fill all required fields.';
      return;
    }

    const formData = new FormData();
    formData.append('title', this.title);
    formData.append('content', this.content);
    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    const url = 'http://localhost:8080/post/add-post';

    this.http.post(url, formData, { withCredentials: true }).subscribe({
      next: (response) => {
        console.log(' Post created:', response);
        this.err = '';
        this.title = '';
        this.content = '';
        this.selectedFile = null;
        this.imagePreview = null;
      },
      error: (error) => {
        console.error('Error creating post:', error);
        this.err =
          error?.error?.errors?.[0]?.message || 'Failed to create post.';
      },
    });
  }
}
