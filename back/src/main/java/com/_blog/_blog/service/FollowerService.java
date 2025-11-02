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

    public FollowerEntity followUser(IdDto followId, AuthEntity currentUser) {
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
            return followerRepo.save(followerObject);
        }

        followerRepo.delete(existingFollow.get());
        return existingFollow.get();
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
}
