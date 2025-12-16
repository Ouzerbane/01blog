package com._blog._blog.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostAdminDto {

    private UUID id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private AuthorDto author;
    private String status ;
  
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDto {
        private UUID id;
        private String username;
        private String email;
    }
}
