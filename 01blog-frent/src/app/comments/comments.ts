import { Component } from '@angular/core';
import { Posts } from '../posts/posts';
import { PostService } from '../services/post';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-comments',
  imports: [CommonModule, FormsModule],
  templateUrl: './comments.html',
  styleUrl: './comments.css',
})
export class Comments {
   constructor(public post:PostService) { }
}
