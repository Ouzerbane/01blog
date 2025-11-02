package com._blog._blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.PostsDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.service.PostsService;

import jakarta.validation.Valid;

// import jakarta.servlet.http.HttpServletRequest;
@RequestMapping("/post")
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class Posts {

    @Autowired
    private PostsService emptyService;

    @PostMapping("/add-post")
    public ResponseEntity<ApiResponse<?>> addPost(@Valid @RequestBody PostsDto dto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostsEntity saved = emptyService.savePost(dto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, saved));
    }

    @PostMapping("/edit-post")
    public ResponseEntity<ApiResponse<?>> editPost(@Valid @RequestBody PostsDto dto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostsEntity edit = emptyService.editPost(dto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, edit));
    }

    @PostMapping("/delete-post")
    public ResponseEntity<ApiResponse<?>> deletePost(@RequestBody IdDto iddto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        emptyService.deletePost(iddto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

}
