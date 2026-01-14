import { Component, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/AuthService';
import { ReactiveFormsModule } from '@angular/forms';
import { retry } from 'rxjs';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  username = signal('');
  email = signal('');
  password = signal('');

  errUsername = signal('');
  errEmail = signal('');
  errPassword = signal('');

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onRegister() {
    
    // clear errors
    this.errUsername.set('');
    this.errEmail.set('');
    this.errPassword.set('');

    if(this.username().trim() === '' || this.email().trim() === '' || this.password().trim() === ''){
      if(this.username().trim() === ''){
        this.errUsername.set("Username is required");
      }
      if(this.email().trim() === ''){
        this.errEmail.set("Email is required");
      }
      if(this.password().trim() === ''){
        this.errPassword.set("Password is required");
      }
      return;
    }

    const body = {
      username: this.username(),
      email: this.email(),
      password: this.password(),
    };

    

    this.authService.register(body).pipe(retry(1)).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        const errors = err.error.errors;

        errors.forEach((e: any) => {
          if (e.field === 'username') {
            this.errUsername.set(e.message);
          }
          if (e.field === 'email') {
            this.errEmail.set(e.message);
          }
          if (e.field === 'password') {
            this.errPassword.set(e.message);
          }
        });
      }
    });
  }

  goTologin() {
    this.router.navigate(['/login']);
  }
}