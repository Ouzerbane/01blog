package com._blog._blog.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.print.attribute.standard.Media;

import org.apache.tomcat.util.http.parser.MediaType;
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
import com._blog._blog.model.entity.PostMediaEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.CommentsRepo;
import com._blog._blog.model.repository.FollowerRepo;
import com._blog._blog.model.repository.LikesRepo;
import com._blog._blog.model.repository.NotificationRepo;
import com._blog._blog.model.repository.PostMediaRepo;
import com._blog._blog.model.repository.PostsRepo;
import com._blog._blog.util.NotificationType;
import com.fasterxml.jackson.databind.ObjectMapper;

import aj.org.objectweb.asm.TypeReference;
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
    private PostMediaRepo postMediaRepo;

    @Autowired
    private LikesRepo likesRepo;

    @Autowired
    private CommentsRepo commentsRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public PostsEntity savePost(
            String title,
            String content,
            MultipartFile[] images,
            AuthEntity currentUser) throws IOException, java.io.IOException {

        title = title.trim();
        content = content.trim();

        PostsEntity post = PostsEntity.builder()
                .title(title)
                .content(content)
                .status("show")
                .author(currentUser)
                .build();

        if (images != null && images.length > 0) {
            for (MultipartFile img : images) {
                com._blog._blog.util.MediaType mediaType = getMediaType(img);
                String url = uploadImage(img);

                PostMediaEntity media = PostMediaEntity.builder()
                        .mediaUrl(url)
                        .post(post)
                        .mediaType(mediaType)
                        .build();
                if (post.getMedia() == null) {
                    post.setMedia(new ArrayList<>());
                }

                post.getMedia().add(media); // add media to post
            }
        }

        // Save post
        PostsEntity savedPost = postsRepo.save(post);

        // Notifications
        List<FollowerEntity> followers = followerRepo.findAllByFollowingId(currentUser.getId());

        if (!followers.isEmpty()) {
            List<NotificationEntity> notifications = followers.stream()
                    .map(f -> NotificationEntity.builder()
                            .message(currentUser.getUsername()
                                    + " created a post " + savedPost.getTitle())
                            .user(f.getFollower())
                            .read(false)
                            .postId(savedPost.getId())
                            .type(NotificationType.POST_CREATED)
                            .build())
                    .toList();

            notificationRepo.saveAll(notifications);
        }

        return savedPost;
    }

    public PostsEntity editPost(String title, String content, List<MultipartFile> image, UUID id,
            AuthEntity currentUser, String oldMediaIds)
            throws IOException, java.io.IOException {
        PostsEntity post = postsRepo.findById(id)
                .orElseThrow(() -> new CustomException("post", "Post not found"));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException("authorization", "You are not the author of this post");
        }
        title = title.trim();
        content = content.trim();
        post.setTitle(title);
        post.setContent(content);

        List<UUID> mediaIds = objectMapper.readValue(
                oldMediaIds,
                new com.fasterxml.jackson.core.type.TypeReference<List<UUID>>() {
                });

        // DELETE OLD MEDIA
        Iterator<PostMediaEntity> iterator = post.getMedia().iterator();
        while (iterator.hasNext()) {
            PostMediaEntity media = iterator.next();
            if (!mediaIds.contains(media.getId())) {
                deleteFile(media.getMediaUrl());
                iterator.remove();
                postMediaRepo.delete(media);
            }
        }

        // ADD NEW MEDIA
        if (image != null && !image.isEmpty()) {
            for (MultipartFile img : image) {
                String url = uploadImage(img);
                PostMediaEntity media = PostMediaEntity.builder()
                        .mediaUrl(url)
                        .mediaType(getMediaType(img))
                        .post(post)
                        .build();
                post.getMedia().add(media);
            }
        }

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

    public List<PostsResponseDto> getPosts(AuthEntity currentUser) {

        List<UUID> followingIds = followerRepo.findAllByFollowerId(currentUser.getId())
                .stream()
                .map(f -> f.getFollowing().getId())
                .collect(Collectors.toList());

        followingIds.add(currentUser.getId());

        if (followingIds.isEmpty()) {
            return List.of();
        }

        List<PostsEntity> posts = postsRepo.findPostsWithMediaByAuthors(followingIds, "Hide");

        return posts.stream()
                .map(post -> PostsResponseDto.toPostsResponseDto(post, currentUser.getId(),
                        likesRepo.countByPostId(post.getId()),
                        likesRepo.existsByUserIdAndPostId(currentUser.getId(), post.getId()),
                        commentsRepo.countByPostId(post.getId())))
                .collect(Collectors.toList());

    }

    public String uploadImage(MultipartFile image) throws IOException, java.io.IOException {

        if (image == null || image.isEmpty()) {
            return null;
        }

        long maxSize = 5 * 1024 * 1024;
        if (image.getSize() > maxSize) {
            throw new CustomException("image", "Image size must be less than 5MB");
        }

        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();// normalize for rm . and ..
        Files.createDirectories(uploadDir);
        System.out.println("------------->" + image.getSize());

        String originalFileName = Paths.get(image.getOriginalFilename()).getFileName().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String fileName = UUID.randomUUID() + extension;
        Path targetLocation = uploadDir.resolve(fileName);

        Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + fileName;
    }

    public com._blog._blog.util.MediaType getMediaType(MultipartFile image) {
        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/jpg", "video/mp4");
        if (!allowedTypes.contains(image.getContentType())) {
            throw new CustomException("mediaType", "Invalid media type: " + image.getContentType());
        }

        if (image.getContentType().startsWith("image/")) {
            return com._blog._blog.util.MediaType.IMAGE; // or other image types based on actual content type
        } else if (image.getContentType().equals("video/mp4")) {
            return com._blog._blog.util.MediaType.VIDEO;
        } else {
            throw new CustomException("mediaType", "Unsupported media type: " + image.getContentType());
        }
    }


    private void deleteFile(String mediaUrl) {
    try {
        if (mediaUrl == null || mediaUrl.isBlank()) {
            return;
        }

        String filePathStr = mediaUrl.replaceFirst("^/", "");

        Path filePath = Paths.get(filePathStr)
                .toAbsolutePath()
                .normalize();

        Files.deleteIfExists(filePath);

    } catch (Exception e) {
        System.out.println("Failed to delete file: " + mediaUrl);
        e.printStackTrace();
    }
}


}
