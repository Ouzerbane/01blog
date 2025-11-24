package com._blog._blog.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
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
    // PostsService(AuthRepo authRepo) {
    // this.authRepo = authRepo;
    // }

    public PostsEntity savePost(PostsDto postsDto, AuthEntity currentUser) {

        PostsEntity post = postsDto.toEntity();
        post.setAuthor(currentUser);
        post = postsRepo.save(post);
        List<FollowerEntity> followers = followerRepo.findAllByFollowingId(currentUser.getId());
        for (FollowerEntity f : followers) {
            NotificationEntity notification = NotificationEntity.builder()
                    .message(currentUser.getUsername() + " created a post " + post.getTitle())
                    .user(f.getFollower())
                    .read(false)
                    .postId(post.getId())
                    .type(NotificationType.POST_CREATED)
                    .build();
            notificationRepo.save(notification);
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

        if (currentUser.getType().equalsIgnoreCase("admin")) {
            return postsRepo.findAllByOrderByCreatedAtDesc(pageable)
                    .map(post -> PostsEntity.toPostsResponseDto(post, post.getAuthor().getId(),
                    likesRepo.countByPostId(post.getId()),
                    likesRepo.existsByUserIdAndPostId(currentUser.getId(), post.getId()),
                    commentsRepo.countByPostId(post.getId())));
        }

        List<Long> followingIds = followerRepo.findAllByFollowerId(currentUser.getId())
                .stream()
                .map(f -> f.getFollowing().getId())
                .collect(Collectors.toList());

        followingIds.add(currentUser.getId());

        if (followingIds.isEmpty()) {
            return Page.empty(pageable);
        }

        return postsRepo.findByAuthorIdInAndStatusNotOrderByCreatedAtDesc(followingIds, "hide", pageable)
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
