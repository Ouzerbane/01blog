import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  username = '';
  email = '';
  password = '';
  errorMessage = '';
  successMessage = '';

  constructor(private http: HttpClient, private router: Router) {}

  register() {
    const payload = {
      username: this.username,
      email: this.email,
      password: this.password,
    };

    this.http.post('http://localhost:8080/register', payload).subscribe({next: () => {
        this.successMessage = 'Registration successful!';
        this.errorMessage = '';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        console.log(err);
        
        this.errorMessage =

          err.error?.errors?.[0]?.message || 'Registration failed.';
        this.successMessage = '';
      },
    });
  }
}
