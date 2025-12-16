interface AuthorDto {
  id: string;
  username: string;
  email: string;
}

export interface Post {
  id: string;
  title: string;
  content: string;
  imageUrl?: string | null;
  createdAt: string;
  canEditAndDelet: string;
  countLike: number;
  countCommets: number;
  like: boolean;
  author: AuthorDto;
  showComment: boolean;
  comment: Comment[];
  isEditing?: boolean;
  editTitle?: string;
  editContent?: string;
  newComment?: string;
  showMenu : boolean ;
  showConfirm:boolean;

  previewImage: null;
  newImage: null

}


export interface Comment {
  id: string;
  postId: string;
  userId: string;
  username: string;
  content: string;
  createdAt: string;

}


export interface PostsResponse {
  success: boolean;
  errors: any;
  data: Post[];
}

export interface Suggested {
  id: string;
  followed: boolean;
  username: string;
  imageUrl? : string  | null
}

export interface SuggestedResponse {
  success: boolean;
  errors: any;
  data: Suggested[];
}

export interface FollowuserResponse {
  success: boolean;
  errors: any;
  data: Suggested;
}

export interface NotificationModul {
  id: boolean;
  message: string;
}

export interface FollowResponse {
  success: boolean;
  errors: any;
  data: {
    followers: number;
    following: number;
    notification : number
  };
}
