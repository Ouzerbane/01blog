package com._blog._blog.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com._blog._blog.dto.PostMediaDto;
import com._blog._blog.dto.PostsResponseDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    // private String imageUrl;

    private String status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private AuthEntity author;

    @OneToMany(mappedBy = "post", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<PostMediaEntity> media = new java.util.ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}

// @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval =
// true)
// @JsonBackReference
// private List<LikesEntity> likes = new ArrayList<>();

// @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval =
// true)
// @JsonBackReference
// @org.hibernate.annotations.OnDelete(action =
// org.hibernate.annotations.OnDeleteAction.CASCADE)
// private List<CommentsEntity> comments = new ArrayList<>();

// bach kol ma ttsjjel post, ttsjjel b date jdida