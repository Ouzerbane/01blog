package com._blog._blog.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.CommentsDto;
import com._blog._blog.dto.DeletCpmmentDto;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.LikeDto;
import com._blog._blog.dto.PostsDto;
import com._blog._blog.dto.PostsResponseDto;
import com._blog._blog.dto.ResponsCommetDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.service.CommentsService;
import com._blog._blog.service.LikesService;
import com._blog._blog.service.PostsService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class Posts {

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private PostsService emptyService;

    @Autowired
    private LikesService likesService;

    // ADD POST
    @PostMapping(value = "/add-post", consumes = { "multipart/form-data" })
    public ResponseEntity<ApiResponse<?>> addPost(
            @RequestParam("title") @NotBlank(message = "title is required") String title,
            @RequestParam("content") @NotBlank(message = "content is required") String content,
            @RequestParam(value = "image", required = false) MultipartFile[] image) throws IOException {

        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        PostsEntity savedPost = emptyService.savePost(title, content, image, currentUser);

        return ResponseEntity.ok(new ApiResponse<>(true, null, savedPost));
    }

    // SERVE FILES
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path file = Paths.get("uploads").resolve(filename).normalize();
        Resource resource = new UrlResource(file.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // EDIT POST
    @PutMapping(value = "/edit-post", consumes = { "multipart/form-data" })
    public ResponseEntity<ApiResponse<?>> editPost(
            @RequestParam("id") @NotNull(message = "id is required") UUID id,
            @RequestParam("title") @NotBlank(message = "title is required") String title,
            @RequestParam("content") @NotBlank(message = "content is required") String content,
            @RequestParam(required = false) List<MultipartFile> image,
            @RequestParam("oldMediaIds") String oldMediaIds) throws IOException {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PostsEntity edit = emptyService.editPost(title, content, image, id, currentUser , oldMediaIds);
        return ResponseEntity.ok(new ApiResponse<>(true, null, edit));
    }

    // DELETE POST
    @DeleteMapping("/delete-post")
    public ResponseEntity<ApiResponse<?>> deletePost(@RequestBody IdDto idDto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        emptyService.deletePost(idDto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

    // GET POSTS
    @GetMapping("/get-posts")
    public ResponseEntity<ApiResponse<?>> getPosts() {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<PostsResponseDto> posts = emptyService.getPosts(currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, posts));
    }

    // LIKE POST
    @PostMapping("/like-post")
    public ResponseEntity<ApiResponse<?>> likePost(@RequestBody IdDto idDto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LikeDto likeDto = likesService.toggleLike(idDto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, likeDto));
    }

    // ADD COMMENT
    @PostMapping("/add-comments")
    public ResponseEntity<ApiResponse<?>> commentPost(@Valid @RequestBody CommentsDto comment) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponsCommetDto count = commentsService.addComment(comment, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, count));
    }

    // GET COMMENTS
    @GetMapping("/get-comments")
    public ResponseEntity<ApiResponse<?>> getComment(@RequestParam("postId") UUID postId) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ResponsCommetDto> comments = commentsService.getComment(new IdDto(postId) , currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, comments));
    }

    @DeleteMapping("/delete-comment")
    public ResponseEntity<ApiResponse<?>> deleteComment(@RequestBody DeletCpmmentDto idDto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long count = commentsService.deleteComment(idDto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, count));
    }
}
