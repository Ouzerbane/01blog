import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-regester',
  imports: [FormsModule,HttpClientModule,CommonModule],
  templateUrl: './regester.html',
  styleUrl: './regester.css',
})
export class Regester {
  info = {
    username: "",
    email: "",
    password: "",
  }
  errpassword = "";
  erremail = "";
  errusername="";
  constructor(private http: HttpClient, private router: Router) { }
  sendRequest() {
    const ipa = "http://localhost:8080/regester";
    this.http.post(ipa, this.info).subscribe((resp) => {
      this.router.navigate(["/login"])
    }, (err) => {
      console.log(err);
      
         this.errusername = "";
        this.erremail = "";
        this.errpassword = "";

        if (err.error && err.error.errors) {
          for (let e of err.error.errors) {
            if (e.field === "email") this.erremail = e.message;
            if (e.field === "password") {
              this.errpassword += e.message + " ";
            }
            if (e.field === "username") this.errusername = e.message;
          }
        }
      
    })
  }

  login(){
    this.router.navigate(["/login"])
  }


}
