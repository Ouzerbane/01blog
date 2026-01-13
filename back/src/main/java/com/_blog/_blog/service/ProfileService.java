package com._blog._blog.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com._blog._blog.dto.PostsResponseDto;
import com._blog._blog.dto.UserDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.exception.NotFoundException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.CommentsRepo;
import com._blog._blog.model.repository.FollowerRepo;
import com._blog._blog.model.repository.LikesRepo;
import com._blog._blog.model.repository.PostsRepo;
import com._blog._blog.util.MediaType;
import com._blog._blog.util.utils.Postutil;

@Service
public class ProfileService {

    @Autowired
    private PostsRepo postsRepo;

    @Autowired
    private LikesRepo likesRepo;
    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private FollowerRepo followerRepo;

    @Autowired
    private CommentsRepo commentsRepo;

    public List<PostsResponseDto> getPostsByUserId(UUID userId, UUID currentUserId) {
        AuthEntity user = authRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("user", "user not found"));

        List<PostsEntity> posts = postsRepo.findByAuthorIdWithMedia(userId, "Hide");
        return posts.stream().map(post -> {
            Long countLikes = likesRepo.countByPostId(post.getId());
            Long countComments = commentsRepo.countByPostId(post.getId());
            boolean isLiked = likesRepo.existsByUserIdAndPostId(currentUserId, post.getId());

            return PostsResponseDto.toPostsResponseDto(post, currentUserId, countLikes, isLiked, countComments);
        }).collect(Collectors.toList());
    }

    public UserDto UserInfo(UUID userId) {
        AuthEntity userinfo = authRepo.findById(userId).orElseThrow(() -> new NotFoundException("user", "user not found"));
        Long followers = followerRepo.countByFollowingId(userId);
        Long fllowing = followerRepo.countByFollowerId(userId);
        System.out.println(userinfo.getImageUrl());
        return UserDto.builder().email(userinfo.getEmail()).username(userinfo.getUsername()).imagUrl(userinfo.getImageUrl()).followers(followers).following(fllowing).build();

    }

    public String updateProfileImage(AuthEntity user, MultipartFile image) throws java.io.IOException {

        MediaType type = Postutil.getMediaType(image);
        if (!type.toString().equals("IMAGE")){
            throw new CustomException("profile","profile moset be just image");
        }



        // String uploadDir = "uploads/";
        // File dir = new File(uploadDir);
        // if (!dir.exists()) {
        //     dir.mkdirs();
        // }

        // String fileName = user.getId() + "_" + System.currentTimeMillis() + "_" + image.getOriginalFilename();
        // Path dest = Paths.get(uploadDir + fileName);
        // Files.copy(image.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = Postutil.uploadImage(image);
        // if (){
            Postutil.deleteFile(user.getImageUrl());

        // }

        AuthEntity userinfo = authRepo.findById(user.getId()).orElseThrow(() -> new CustomException("user", "user not found"));
        userinfo.setImageUrl(imageUrl);
        authRepo.save(userinfo);
        return imageUrl;
    }

}
