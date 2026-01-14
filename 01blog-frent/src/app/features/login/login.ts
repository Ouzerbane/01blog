import { Component, signal } from '@angular/core';
import { AuthService } from '../../services/AuthService';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  usernameOrEmail = signal("");
  password = signal("");
  err = signal("");

  constructor(private authservic: AuthService
    ,private router : Router , private snackbar: MatSnackBar
  ){}

  onLogin(){
    if(this.usernameOrEmail() === "" || this.usernameOrEmail().trim() === "" || this.password() === "" || this.password().trim() === ""){
      this.err.set("All fields are required");
      return;
    }
    if (this.password().length < 8 || this.password().length > 30) {
      this.err.set("Password must be at least 8 characters long");
      return;
    }
    if (this.usernameOrEmail().trim().length < 3 || this.usernameOrEmail().trim().length > 50) {
      this.err.set("Username or Email must be at least 3 characters long");
      return;
    }
    const body = {
      usernameOrEmail:this.usernameOrEmail().trim(),
      password : this.password()
    }    
   this.authservic.login(body).subscribe({
      next: (req) => {
        console.log(req);
        
        this.snackbar.open("Login successful", "Close", { duration: 3000 , panelClass: ['success-snackbar']});
        this.router.navigate(["/"])
      },
      error: (errer) => {
        this.err.set(errer.error.errors[0].message)
      }
    });
  }

  goToregester(){
    this.router.navigate(["/register"])
  }
}
