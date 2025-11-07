package com._blog._blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.FollowCountDto;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.UserFollowDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.FollowerEntity;
import com._blog._blog.service.FollowerService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class Follower {

    @Autowired
    private FollowerService followerService;

    @PostMapping("/follow")
    public ResponseEntity<ApiResponse<?>> followUser(@Valid @RequestBody IdDto dto) {

        AuthEntity currentUser = (AuthEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        FollowerEntity result = followerService.followUser(dto, currentUser);

        return ResponseEntity.ok(new ApiResponse<>(true, null, result));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> getUsers() {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<UserFollowDto> users = followerService.getUsersWithFollowStatus(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, null, users));
    }

    @GetMapping("/follow-counts")
    public ResponseEntity<ApiResponse<FollowCountDto>> getFollowCounts() {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long followers = followerService.getCountFollowers(currentUser.getId());
        Long following = followerService.getCountFollowing(currentUser.getId());

        FollowCountDto counts = new FollowCountDto(followers, following);

        return ResponseEntity.ok(new ApiResponse<>(true, null, counts));
    }

    @GetMapping("/suggested")
    public ResponseEntity<ApiResponse<List<UserFollowDto>>> getSuggestedUsers() {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<UserFollowDto> suggestions = followerService.getUsersSuggested(currentUser.getId());

        return ResponseEntity.ok(new ApiResponse<>(true, null, suggestions));
    }

}
