package com._blog._blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.CommentsDto;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.LikeDto;
import com._blog._blog.dto.PostsDto;
import com._blog._blog.dto.PostsResponseDto;
import com._blog._blog.dto.ResponsCommetDto;
import com._blog._blog.dto.ReturnCommentDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.service.CommentsService;
import com._blog._blog.service.LikesService;
import com._blog._blog.service.PostsService;

import jakarta.validation.Valid;

// import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")


public class Posts {
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private PostsService emptyService;

    @Autowired
    private LikesService likesservice;

    @PostMapping("/add-post")
    public ResponseEntity<ApiResponse<?>> addPost(@Valid @RequestBody PostsDto dto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostsEntity saved = emptyService.savePost(dto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

    @PutMapping("/edit-post")
    public ResponseEntity<ApiResponse<?>> editPost(@Valid @RequestBody PostsDto dto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostsEntity edit = emptyService.editPost(dto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, edit));
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<ApiResponse<?>> deletePost(@RequestBody IdDto iddto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        emptyService.deletePost(iddto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

    @GetMapping("/get-posts")
    public ResponseEntity<ApiResponse<?>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Page<PostsResponseDto> posts = emptyService.getPosts(page, size, currentUser);
    //    System.out.print("ppppppppp"+posts.getContent());
        return ResponseEntity.ok(new ApiResponse<>(true, null, posts.getContent()));
    }

    @PostMapping("/like-post")
    public ResponseEntity<ApiResponse<?>> likePost(@RequestBody IdDto iddto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LikeDto LikeDto = likesservice.toggleLike(iddto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, LikeDto));
    }

    @PostMapping("/add-comments")
    public ResponseEntity<ApiResponse<?>> commentPost(@RequestBody CommentsDto comment) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReturnCommentDto count = commentsService.addComment(comment, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, count));
    }

@GetMapping("/get-comments")
public ResponseEntity<ApiResponse<?>> getComment(@RequestParam("postId") Long postId) {
    List<ResponsCommetDto> comments = commentsService.getComment(new IdDto(postId));
    return ResponseEntity.ok(new ApiResponse<>(true, null, comments));
}

}
