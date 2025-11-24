package com._blog._blog.dto;

import java.time.LocalDateTime;

import com._blog._blog.model.entity.PostsEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "title is required")
    @Size(min = 3, max = 100, message = "title must be between 3 and 2000 characters")
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

    public PostsEntity toEntity() {
        PostsEntity post = new PostsEntity();
        post.setId(this.id);
        post.setTitle(this.title);
        post.setContent(this.content);
        post.setImageUrl(this.imageUrl);
        post.setStatus("show");
        post.setCreatedAt(this.createdAt != null ? this.createdAt : LocalDateTime.now());
        // author khasha ttsawb mn service (ma kansiftouch mn frontend)
        return post;
    }
}
