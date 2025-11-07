package com._blog._blog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.UserFollowDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.FollowerEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.FollowerRepo;

@Service
public class FollowerService {

    @Autowired
    private FollowerRepo followerRepo;

    @Autowired
    private AuthRepo authRepo;

    public UserFollowDto followUser(IdDto followId, AuthEntity currentUser) {
        AuthEntity otherUser = authRepo.findById(followId.getId())
                .orElseThrow(() -> new CustomException("user", "User not found"));

        if (otherUser.getId().equals(currentUser.getId())) {
            throw new CustomException("follow", "You cannot follow yourself");
        }

        Optional<FollowerEntity> existingFollow = followerRepo.findByFollowerIdAndFollowingId(currentUser.getId(),
                otherUser.getId());

        if (existingFollow.isEmpty()) {

            FollowerEntity followerObject = FollowerEntity.builder()
                    .follower(currentUser)
                    .following(otherUser)
                    .build();
            followerRepo.save(followerObject);

            return UserFollowDto.builder()
                    .id(followerObject.getId())
                    .username(otherUser.getUsername())
                    .followed(true)
                    .build();
        } else {

            followerRepo.delete(existingFollow.get());

            return UserFollowDto.builder()
                    .id(existingFollow.get().getId())
                    .username(otherUser.getUsername())
                    .followed(false)
                    .build();
        }
    }

    public List<UserFollowDto> getUsersWithFollowStatus(Long currentUserId) {

        List<AuthEntity> users = authRepo.findAll()
                .stream()
                .filter(u -> !u.getId().equals(currentUserId))
                .collect(Collectors.toList());

        return users.stream().map(user -> {
            boolean isFollowed = followerRepo.existsByFollowerIdAndFollowingId(currentUserId, user.getId());
            return new UserFollowDto(user.getId(), user.getUsername(), isFollowed);
        }).collect(Collectors.toList());
    }

    public Long getCountFollowers(Long currentUserId) {

        return followerRepo.countByFollowingId(currentUserId);
    }

    public Long getCountFollowing(Long currentUserId) {

        return followerRepo.countByFollowerId(currentUserId);
    }

    public List<UserFollowDto> getUsersSuggested(Long currentUserId) {

        List<AuthEntity> allUsers = authRepo.findAll()
                .stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .collect(Collectors.toList());

        List<FollowerEntity> followingList = followerRepo.findByFollowerId(currentUserId);

        List<Long> followingIds = followingList.stream()
                .map(f -> f.getFollowing().getId())
                .collect(Collectors.toList());

        List<AuthEntity> suggestedUsers = allUsers.stream()
                .filter(user -> !followingIds.contains(user.getId()))
                .collect(Collectors.toList());

        return suggestedUsers.stream()
                .map(user -> new UserFollowDto(user.getId(), user.getUsername(), false))
                .collect(Collectors.toList());
    }

    public List<UserFollowDto> getFollowersService(Long currentUserId) {
        List<FollowerEntity> followers = followerRepo.findAllByFollowingId(currentUserId);

        return followers.stream()
                .map(f -> {
                    boolean isFollowed = followerRepo.existsByFollowerIdAndFollowingId(currentUserId,f.getFollower().getId());
                    return new UserFollowDto(
                            f.getFollower().getId(),
                            f.getFollower().getUsername(),
                            isFollowed);
                })
                .collect(Collectors.toList());
    }

    public List<UserFollowDto> getFollowingService(Long userId) {
        List<FollowerEntity> followingList = followerRepo.findAllByFollowerId(userId);

        return followingList.stream()
                .map(f -> new UserFollowDto(
                        f.getFollowing().getId(),
                        f.getFollowing().getUsername(),
                        true))
                .collect(Collectors.toList());
    }

}
