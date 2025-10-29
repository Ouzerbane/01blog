import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule]  // hna khas tdir import dyal kolchi li katst3ml
})
export class Login {
  usernameOrEmail: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    const payload = {
      usernameOrEmail: this.usernameOrEmail,
      password: this.password
    };

    this.http.post('http://localhost:8080/login', payload, { withCredentials: true }).subscribe({
      next: () => {
        this.router.navigate(['/register']);
      },
      error: (err) => {
        this.errorMessage = err.error?.errors?.[0]?.message || 'Login failed.';
      }
    });
  }
}
