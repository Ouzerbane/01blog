package com._blog._blog.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.PostsDto;
import com._blog._blog.dto.PostsResponseDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.FollowerEntity;
import com._blog._blog.model.entity.NotificationEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.CommentsRepo;
import com._blog._blog.model.repository.FollowerRepo;
import com._blog._blog.model.repository.LikesRepo;
import com._blog._blog.model.repository.NotificationRepo;
import com._blog._blog.model.repository.PostsRepo;
import com._blog._blog.util.NotificationType;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;

@Service
public class PostsService {
    // @Autowired
    // private final AuthRepo authRepo;

    @Autowired
    private FollowerRepo followerRepo;

    @Autowired
    private PostsRepo postsRepo;

    @Autowired
    private LikesRepo likesRepo;

    @Autowired
    private CommentsRepo commentsRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Transactional
    public PostsEntity savePost(String title, String content, MultipartFile image, AuthEntity currentUser)
            throws IOException, java.io.IOException {

        title = title != null ? title.trim() : "";
        content = content != null ? content.trim() : "";
        if (title.length() < 3 || content.length() < 3) {
            throw new CustomException("title/content", "Title and content must be at least 3 characters");
        }

        PostsDto postsDto = new PostsDto();

        postsDto.setTitle(title);

        postsDto.setContent(content);

        String imageUrl = uploadImage(image);

        PostsEntity post = PostsEntity.builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .status("show")
                .createdAt(LocalDateTime.now())
                .author(currentUser)
                .build();

        post = postsRepo.save(post);
        PostsEntity postlambda = post;

        /// Create notifications in batch
        List<FollowerEntity> followers = followerRepo.findAllByFollowingId(currentUser.getId());
        List<NotificationEntity> notifications = followers.stream()
                .map(f -> NotificationEntity.builder()
                        .message(currentUser.getUsername() + " created a post " + postlambda.getTitle())
                        .user(f.getFollower())
                        .read(false)
                        .postId(postlambda.getId())
                        .type(NotificationType.POST_CREATED)
                        .build())
                .collect(Collectors.toList());

        if (!notifications.isEmpty()) {
            notificationRepo.saveAll(notifications);
        }

        return post;
    }

    public PostsEntity editPost(PostsDto postsDto, AuthEntity currentUser) {
        PostsEntity post = postsRepo.findById(postsDto.getId())
                .orElseThrow(() -> new CustomException("post", "Post not found"));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException("authorization", "You are not the author of this post");
        }

        post.setTitle(postsDto.getTitle());
        post.setContent(postsDto.getContent());
        post.setImageUrl(postsDto.getImageUrl());
        return postsRepo.save(post);
    }

    public void deletePost(IdDto idDto, AuthEntity currentUser) {
        PostsEntity post = postsRepo.findById(idDto.getId())
                .orElseThrow(() -> new CustomException("post", "Post not found"));
        if (currentUser.getType().toLowerCase().equals("admin")) {
            postsRepo.delete(post);
            return;
        }
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException("authorization", "You are not the author of this post");
        }
        postsRepo.delete(post);
    }

    public Page<PostsResponseDto> getPosts(int page, int size, AuthEntity currentUser) {
        Pageable pageable = PageRequest.of(page, size);

        List<UUID> followingIds = followerRepo.findAllByFollowerId(currentUser.getId())
                .stream()
                .map(f -> f.getFollowing().getId())
                .collect(Collectors.toList());

        followingIds.add(currentUser.getId());

        if (followingIds.isEmpty()) {
            return Page.empty(pageable);
        }

        return postsRepo.findByAuthorIdInAndStatusNotOrderByCreatedAtDesc(followingIds, "Hide", pageable)
                .map(post -> PostsEntity.toPostsResponseDto(post, currentUser.getId(),
                        likesRepo.countByPostId(post.getId()),
                        likesRepo.existsByUserIdAndPostId(currentUser.getId(), post.getId()),
                        commentsRepo.countByPostId(post.getId())));
        // ::
    }

    public String uploadImage(MultipartFile image) throws IOException, java.io.IOException {

        if (image == null || image.isEmpty()) {
            return null; // no image
        }

        String uploadDir = "uploads/";
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path dest = Paths.get(uploadDir + fileName);

        Files.copy(image.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + fileName;
    }

}
