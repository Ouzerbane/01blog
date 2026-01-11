package com._blog._blog.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.UserFollowDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.exception.NotFoundException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.FollowerEntity;
import com._blog._blog.model.entity.NotificationEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.FollowerRepo;
import com._blog._blog.model.repository.NotificationRepo;
import com._blog._blog.util.NotificationType;

import jakarta.transaction.Transactional;

@Service
public class FollowerService {

    @Autowired
    private FollowerRepo followerRepo;

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Transactional
    public UserFollowDto followUser(IdDto followId, AuthEntity currentUser) {

        AuthEntity otherUser = authRepo.findById(followId.getId())
                .orElseThrow(() -> new NotFoundException("user", "User not found"));

        if (otherUser.getId().equals(currentUser.getId())) {
            throw new CustomException("follow", "You cannot follow yourself");
        }

        Optional<FollowerEntity> existingFollow = followerRepo
                .findByFollowerIdAndFollowingId(currentUser.getId(), otherUser.getId());

        if (existingFollow.isEmpty()) {

            FollowerEntity followerObject = FollowerEntity.builder()
                    .follower(currentUser)
                    .following(otherUser)
                    .build();
            followerRepo.save(followerObject);

            NotificationEntity notification = NotificationEntity.builder()
                    .message(currentUser.getUsername() + " started following you")
                    .user(otherUser)
                    .sender(currentUser)
                    .type(NotificationType.FOLLOW)
                    .read(false)
                    .build();

            notificationRepo.save(notification);

            return UserFollowDto.builder()
                    .id(followerObject.getId())
                    .username(otherUser.getUsername())
                    .followed(true)
                    .build();
        }

        followerRepo.delete(existingFollow.get());
        if (notificationRepo.existsBySenderIdAndUserIdAndType(currentUser.getId(), otherUser.getId(), NotificationType.FOLLOW)) {
            notificationRepo.deleteBySenderIdAndUserIdAndType(
                    currentUser.getId(),
                    otherUser.getId(),
                    NotificationType.FOLLOW
            );
        }

        return UserFollowDto.builder()
                .id(existingFollow.get().getId())
                .username(otherUser.getUsername())
                .followed(false)
                .build();
    }

    public List<UserFollowDto> getUsersWithFollowStatus(UUID currentUserId) {

        List<AuthEntity> users = authRepo.findAll()
                .stream()
                .filter(u -> !u.getId().equals(currentUserId))
                .collect(Collectors.toList());

        return users.stream().map(user -> {
            boolean isFollowed = followerRepo.existsByFollowerIdAndFollowingId(currentUserId, user.getId());
            return new UserFollowDto(user.getId(), user.getUsername(), user.getImageUrl(), isFollowed);
        }).collect(Collectors.toList());
    }

    public Long getCountFollowers(UUID currentUserId) {

        return followerRepo.countByFollowingId(currentUserId);
    }

    public Long getCountFollowing(UUID currentUserId) {

        return followerRepo.countByFollowerId(currentUserId);
    }

    public List<UserFollowDto> getUsersSuggested(UUID currentUserId) {

        List<AuthEntity> allUsers = authRepo.findAll()
                .stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .collect(Collectors.toList());

        List<FollowerEntity> followingList = followerRepo.findByFollowerId(currentUserId);

        List<UUID> followingIds = followingList.stream()
                .map(f -> f.getFollowing().getId())
                .collect(Collectors.toList());

        List<AuthEntity> suggestedUsers = allUsers.stream()
                .filter(user -> !followingIds.contains(user.getId()))
                .collect(Collectors.toList());

        return suggestedUsers.stream()
                .map(user -> new UserFollowDto(user.getId(), user.getUsername(), user.getImageUrl(),
                false))
                .collect(Collectors.toList());
    }

    public List<UserFollowDto> getFollowersService(UUID profileUserId, AuthEntity loginUser) {
        authRepo.findById(profileUserId)
                .orElseThrow(() -> new NotFoundException("user", "User not found"));

        List<FollowerEntity> followers = followerRepo.findAllByFollowingId(profileUserId);

        return followers.stream()
                .map(f -> {
                    boolean isFollowed = followerRepo.existsByFollowerIdAndFollowingId(
                            loginUser.getId(), f.getFollower().getId());
                    return new UserFollowDto(
                            f.getFollower().getId(),
                            f.getFollower().getUsername(),
                            f.getFollower().getImageUrl(),
                            isFollowed);
                })
                .collect(Collectors.toList());
    }

    public List<UserFollowDto> getFollowingService(UUID profileUserId, AuthEntity loginUser) {
        authRepo.findById(profileUserId)
                .orElseThrow(() -> new NotFoundException("user", "User not found"));

        List<FollowerEntity> followingList = followerRepo.findAllByFollowerId(profileUserId);

        return followingList.stream()
                .map(f -> {
                    boolean isFollowed = followerRepo.existsByFollowerIdAndFollowingId(
                            loginUser.getId(), f.getFollowing().getId());
                    return new UserFollowDto(
                            f.getFollowing().getId(),
                            f.getFollowing().getUsername(),
                            f.getFollowing().getImageUrl(),
                            isFollowed);
                })
                .collect(Collectors.toList());
    }

    public List<UserFollowDto> serchService(String input, AuthEntity loginUser) {

        return authRepo.findByUsernameContainingIgnoreCase(input)
                .stream()
                .filter(user -> !user.getId().equals(loginUser.getId()))
                .map(user -> UserFollowDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .imageUrl(user.getImageUrl())
                .followed(followerRepo.existsByFollowerIdAndFollowingId(loginUser.getId(), user.getId()))
                .build())
                .toList();
    }

}
