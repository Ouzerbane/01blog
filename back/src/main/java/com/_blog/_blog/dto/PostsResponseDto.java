package com._blog._blog.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com._blog._blog.model.entity.PostsEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostsResponseDto {
    private UUID id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private AuthorDto author;
    private boolean canEditAndDelet;
    private Long countLike;
    private Long countCommets;
    private boolean like;
    private List<PostMediaDto> media;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDto {
        private UUID id;
        private String username;
        private String email;
    }

    public static PostsResponseDto toPostsResponseDto(PostsEntity post, UUID currentUserId, Long countLikes,
            boolean islike, Long counComment) {
        boolean canEdit = post.getAuthor() != null && post.getAuthor().getId().equals(currentUserId);

        List<PostMediaDto> mediaDtos = post.getMedia().stream()
                .map(media -> PostMediaDto.builder()
                        .id(media.getId())
                        .url(media.getMediaUrl())
                        .build())
                .toList();
        return PostsResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .media(mediaDtos)
                .createdAt(post.getCreatedAt())
                .author(PostsResponseDto.AuthorDto.builder()
                        .id(post.getAuthor().getId())
                        .username(post.getAuthor().getUsername())
                        .email(post.getAuthor().getEmail())
                        .build())
                .canEditAndDelet(canEdit)
                .like(islike)
                .countLike(countLikes)
                .countCommets(counComment)
                .build();
    }

}
