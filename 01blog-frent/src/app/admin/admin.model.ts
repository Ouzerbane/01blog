export interface UserAdminDto {
  id: number;
  username: string;
  imageUrl: string;
  action: string;
  ban: String;
  showConfirm: boolean;

}


export interface ReportAdminDto {
  id: number;
  reporter: string;
  targetUser: string;
  reason: string;
}

export interface PostsAdminDto {
  id: number;
  title: string;
  content: string;
  imageUrl: string;
  createdAt: string;
  status: string;
  author: UserDto;
  showConfirm: boolean;

}

export interface UserDto {
  id: number;
  username: string;
  email: string;
}

