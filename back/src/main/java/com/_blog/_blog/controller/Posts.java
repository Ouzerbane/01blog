package com._blog._blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.CommentsDto;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.PostsDto;
import com._blog._blog.dto.ReturnCommentDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.service.CommentsService;
import com._blog._blog.service.LikesService;
import com._blog._blog.service.PostsService;

import jakarta.validation.Valid;

// import jakarta.servlet.http.HttpServletRequest;
@RequestMapping("/post")
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class Posts {
    @Autowired
    private CommentsService commentsService ;
    @Autowired
    private PostsService emptyService;

    @Autowired
    private LikesService likesservice;

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

    @GetMapping("/get-posts")
    public ResponseEntity<ApiResponse<?>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostsEntity> posts = emptyService.getPosts(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, null, posts.getContent()));
    }

    @PostMapping("/like-post")
    public ResponseEntity<ApiResponse<?>> likePost(@RequestBody IdDto iddto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long count = likesservice.toggleLike(iddto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, count));
    }

    @PostMapping("/add-comments")
    public ResponseEntity<ApiResponse<?>> commentPost(@RequestBody CommentsDto comment) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReturnCommentDto count = commentsService.addComment(comment, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, count));
    }
}
