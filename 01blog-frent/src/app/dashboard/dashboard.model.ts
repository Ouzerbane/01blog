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
  author: AuthorDto;
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
