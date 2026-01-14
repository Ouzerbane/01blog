interface authorDto {
  id: string;
  username: string;
  email: string;
  imagUrl?: string | null;
}

interface Media {
  id: string;
  type: string;
  url: string;
}


export interface onePost {
  id: string;
  title: string;
  content: string;
  media?: Media[] | null;
  createdAt: string;
  canEditAndDelet: boolean;
  author: authorDto;
  countLike: number;
  countCommets: number;
  like: boolean;
  comment: oneComment[];
}

export interface PostsResponse {
  success: boolean;
  errors: any;
  data: onePost[];
}


export interface oneComment {
  id: string;
  postId: string;
  userId: string;
  username: string;
  content: string;
  url?: string | null;
  createdAt: string;
  candelet: boolean;

}


export interface Suggested {
  id: string;
  followed: boolean;
  username: string;
  imageUrl? : string  | null
}


// export interface Post {
//   id: string;
//   title: string;
//   content: string;
//   media?: string[] | null;
//   createdAt: string;
//   canEditAndDelet: string;
//   countLike: number;
//   countCommets: number;
//   like: boolean;
//   author: AuthorDto;
//   showComment: boolean;
//   comment: Comment[];
//   isEditing?: boolean;
//   editTitle?: string;
//   editContent?: string;
//   newComment?: string;
//   showMenu : boolean ;
//   showConfirm:boolean;

//   previewImage: null;
//   newImage: null

// }
