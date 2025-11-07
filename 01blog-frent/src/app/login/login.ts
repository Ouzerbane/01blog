import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  imports: [FormsModule, HttpClientModule,CommonModule ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  password = "";
  usernameOrEmail = "";
  err = "";
  constructor(private http: HttpClient , private router :Router) { }
  show() {
    console.log(this.password, this.usernameOrEmail);
    const ipa = "http://localhost:8080/login";
    this.http.post(ipa, { password: this.password, usernameOrEmail: this.usernameOrEmail }, {withCredentials:true}).subscribe((respons) => {
      console.log(respons);
      this.router.navigate(['/'])

    }, (errer) => {
      this.err = errer.error.errors[0].message
    })
  }

  regester(){
     this.router.navigate(['/regester'])
  }
}
