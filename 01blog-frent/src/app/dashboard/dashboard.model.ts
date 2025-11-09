interface AuthorDto {
  id: number;
  username: string;
  email: string;
}

interface Post {
  id: number;
  title: string;
  content: string;
  imageUrl: string;
  createdAt: string;
  canEditAndDelet: string;
  countLike: number;
  countCommets: number;
  like: boolean;
  author: AuthorDto;
  showComment : boolean ;
  comment : Comment [];
  isEditing?: boolean;
  editTitle?: string;
  editContent?: string;
  newComment?: string; 

}


interface Comment {
  id: number;            
  postId: number;        
  userId: number;        
  username: string;    
  content: string;      
  createdAt: string;

}


interface PostsResponse {
  success: boolean;
  errors: any;
  data: Post[];
}

interface Suggested {
  id: number;
  followed: boolean;
  username: string;
}

interface SuggestedResponse {
  success: boolean;
  errors: any;
  data: Suggested[];
}

interface FollowuserResponse {
  success: boolean;
  errors: any;
  data: Suggested;
}

interface FollowResponse {
  success: boolean;
  errors: any;
  data: {
    followers: number;
    following: number;
  };
}
