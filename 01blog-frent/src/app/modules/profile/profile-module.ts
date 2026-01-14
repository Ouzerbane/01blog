import { Suggested } from "../home/home-module";

export interface UserDto {
  id: string;
  username: string;
  email: string;
  imageUrl?: string; 
  followers: number;
  following: number;
}

export interface SuggestedResponse {
  success: boolean;
  errors: any;
  data: Suggested[];
}