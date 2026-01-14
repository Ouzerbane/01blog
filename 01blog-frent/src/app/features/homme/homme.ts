import { Component, OnInit, signal } from '@angular/core';
import { HommeService } from '../../services/homme-service';
import { onePost, Suggested } from '../../modules/home/home-module';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { Post } from '../../commponent/post/post';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';

@Component({
  selector: 'app-homme',
  imports: [CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatChipsModule, Post, MatFormFieldModule, MatInputModule],
  templateUrl: './homme.html',
  styleUrl: './homme.css',
})
export class Homme implements OnInit {
  allPosts = signal<onePost[]>([]);
  suggestedUsers = signal<Suggested[]>([]);
  recherchSugestion = signal('');


  constructor(private hommeService: HommeService, private router: Router) { }

  ngOnInit(): void {
    this.getAllPosts();
    this.getSuggestedUsers();
  }

  getAllPosts() {
    this.hommeService.getAllPosts().subscribe({
      next: (req) => {
        this.allPosts.set(req.data.map(post => ({
          ...post,
          media: post.media ? post.media.map(mediaItem => ({
            ...mediaItem,
            url: `http://localhost:8080/post${mediaItem.url}`
          })) : null,
          author: {
            ...post.author,
            imagUrl: post.author.imagUrl ? `http://localhost:8080/post${post.author.imagUrl}` : null
          }
        })));
      }

    });
  }

  recherchUsers() {
    this.hommeService.recherchUsers(this.recherchSugestion()).subscribe({
      next: (req) => {
        this.suggestedUsers.set(req.data.map((user: Suggested) => ({
          ...user,
          imageUrl: user.imageUrl ? `http://localhost:8080/post${user.imageUrl}` : null
        })));
        this.recherchSugestion() === '' ? this.getSuggestedUsers() :
        console.log(req.data);
      }
      });
  }

  getSuggestedUsers() {
    this.hommeService.getSuggestedUsers().subscribe({
      next: (req) => {
        this.suggestedUsers.set(req.data.map((user: Suggested) => ({
          ...user,
          imageUrl: user.imageUrl ? `http://localhost:8080/post${user.imageUrl}` : null
        })));
      }

    });
  }

  followUser(userId: string) {
    this.hommeService.followUser(userId).subscribe({
      next: (req) => {
        console.log(req);
        this.suggestedUsers.set(this.suggestedUsers().map(user => {
          if (user.id === userId) {
            // return req.data;
            return { ...user, followed: req.data.followed };
          }
          return user;
        }));
      }
    });
  }

  onDeletPost(postId: string) {
    this.allPosts.set(this.allPosts().filter(post => post.id !== postId));
  }

   getProfile(id: string) {
    this.router.navigate(["/profile", id]);
  }

  
}
 