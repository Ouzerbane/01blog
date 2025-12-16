package com._blog._blog.dto;

import java.time.LocalDateTime;

import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.PostsEntity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostsDto {

    private Long id;

    private String title;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    private String authorUsername; // bach ma n3tihch l AuthEntity kamla

    public static PostsDto fromEntity(PostsEntity entity) {
        return PostsDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrl(entity.getImageUrl())
                .createdAt(entity.getCreatedAt())
                .authorUsername(entity.getAuthor() != null ? entity.getAuthor().getUsername() : null)
                .build();
    }

}
