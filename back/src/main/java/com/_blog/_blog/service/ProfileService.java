package com._blog._blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.PostsResponseDto;
import com._blog._blog.dto.UserDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.CommentsRepo;
import com._blog._blog.model.repository.FollowerRepo;
import com._blog._blog.model.repository.LikesRepo;
import com._blog._blog.model.repository.PostsRepo;

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

    public List<PostsResponseDto> getPostsByUserId(Long userId, Long currentUserId) {
        List<PostsEntity> posts = postsRepo.findByAuthorIdOrderByCreatedAtDesc(userId);
        return posts.stream().map(post -> {
            Long countLikes = likesRepo.countByPostId(post.getId());
            Long countComments = commentsRepo.countByPostId(post.getId());
            boolean isLiked = likesRepo.existsByUserIdAndPostId(currentUserId, post.getId());

            return PostsEntity.toPostsResponseDto(post, currentUserId, countLikes, isLiked, countComments);
        }).collect(Collectors.toList());
    }

    public UserDto UserInfo(Long userId) {
        AuthEntity userinfo = authRepo.findById(userId).orElseThrow(() -> new CustomException("user", "user not found"));
        Long followers = followerRepo.countByFollowingId(userId);
        Long fllowing = followerRepo.countByFollowerId(userId);

        return UserDto.builder().email(userinfo.getEmail()).username(userinfo.getUsername()).followers(followers).following(fllowing).build();

    }

}
