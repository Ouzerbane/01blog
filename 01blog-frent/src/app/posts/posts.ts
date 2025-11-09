import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-posts',
  imports: [FormsModule,HttpClientModule,CommonModule],
  templateUrl: './posts.html',
  styleUrl: './posts.css',
})
export class Posts {
  imageUrl = "";
  title = "";
  content = "";
  err = "";
  constructor(private http: HttpClient) { }
  onsubmet(){
    // console.log("====>tt",this.imageUrl,this.title,this.content);
    this.imageUrl = "~/Desktop/01blog/01blog-frent/src/app/concept-de-jeu-de-football.jpg"
    const ipa = "http://localhost:8080/post/add-post";
    this.http.post(ipa, { title: this.title, content: this.content ,  imageUrl: this.imageUrl }, {withCredentials:true}).subscribe((respons) => {
      console.log(respons);

    }, (errer) => {
      this.err = errer.error.errors[0].message;
      
    })
  }
  
}
