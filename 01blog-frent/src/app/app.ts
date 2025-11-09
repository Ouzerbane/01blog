import { Component, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Login } from './login/login';
import { Header } from './header/header';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header,CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('01blog-frent');
  constructor(private router: Router) {}

  isAuthPage(): boolean {
    return this.router.url.includes('/login') || this.router.url.includes('/regester');
  }
}
