package com._blog._blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.PostsResponseDto;
import com._blog._blog.dto.UserDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.service.ProfileService;

@RestController
public class Profile {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/get-post-profile/{userId}")
    public ResponseEntity<ApiResponse<?>> getPostsByUser(@PathVariable("userId") Long userId) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<PostsResponseDto> posts = profileService.getPostsByUserId(userId, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, null, posts));
    }

    @GetMapping("/get-userInfo/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserInfo(@PathVariable("userId") Long userId) {
        UserDto posts = profileService.UserInfo(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, null, posts));
    }

   @GetMapping("/me")
public ResponseEntity<ApiResponse<?>> getCurrentUser() {
    AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    IdDto userid = new IdDto(currentUser.getId()); 

    return ResponseEntity.ok(new ApiResponse<>(true, null, userid));
}


}
