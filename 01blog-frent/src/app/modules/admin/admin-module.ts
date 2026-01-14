export interface UserAdminDto {
  id: string;
  username: string;
  imageUrl: string;
  action: string;
  ban: String;
  showConfirm: boolean;
  showConfirmBan : boolean ; 

}



export interface ReportAdminDto {
  id: string;
  reason: string;

  reporter: string;
  reporterId: string;

  targetUser: string;
  targetUserId: string;
  type: string;
  targetPost: string;
  targetPostId: string;
  showConfirm: boolean;
  time:string;
}

export interface PostsAdminDto {
  id: string;
  title: string;
  content: string;
  media: Media[] | null;
  createdAt: string;
  status: string;
  author: UserDto;
  showConfirm: boolean;
  showConfirmHid: boolean;

}


export interface UserDto {
  id: string;
  username: string;
  email: string;
  imageUrl: string | null;
}

export interface Media {
  id: string;
  type: string;
  url: string;
}
