import { Injectable } from '@angular/core';
import { Post } from '../dashboard/dashboard.model';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor (){}
  postd: Post[] = [];
  id : String = "";
}
