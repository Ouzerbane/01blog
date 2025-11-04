package com._blog._blog.model.entity;

import java.time.LocalDateTime;

import com._blog._blog.dto.PostsResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    // relation m3a l user li ktb l post
    @ManyToOne ///
    @JoinColumn(name = "author_id")
    private AuthEntity author;

    // bach kol ma ttsjjel post, ttsjjel b date jdida
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

      public static PostsResponseDto toPostsResponseDto(PostsEntity post) {
          return PostsResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .createdAt(post.getCreatedAt())
                .author(PostsResponseDto.AuthorDto.builder()
                        .id(post.getAuthor().getId())
                        .username(post.getAuthor().getUsername())
                        .email(post.getAuthor().getEmail())
                        .build())
                .build();
    }
}
