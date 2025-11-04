package com._blog._blog.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private AuthorDto author;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDto {
        private Long id;
        private String username;
        private String email;
    }

    // public static PostsResponseDto toPostsResponseDto(PostsEntity post) {
    //       return PostsResponseDto.builder()
    //             .id(post.getId())
    //             .title(post.getTitle())
    //             .content(post.getContent())
    //             .imageUrl(post.getImageUrl())
    //             .createdAt(post.getCreatedAt())
    //             .author(PostsResponseDto.AuthorDto.builder()
    //                     .id(post.getAuthor().getId())
    //                     .username(post.getAuthor().getUsername())
    //                     .email(post.getAuthor().getEmail())
    //                     .build())
    //             .build();
    // }

}
