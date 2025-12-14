package com._blog._blog.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private AuthRepo authRepo;

    // ADD POST
    @PostMapping(value = "/add-post", consumes = { "multipart/form-data" })
    public ResponseEntity<ApiResponse<?>> addPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
               
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        PostsEntity savedPost = emptyService.savePost(title,content , image, currentUser);

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
            @RequestParam("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostsDto dto = PostsDto.builder().content(content).id(id).title(title).imageUrl(emptyService.uploadImage(image))
                .build();
        PostsEntity edit = emptyService.editPost(dto, currentUser);
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
    public ResponseEntity<ApiResponse<?>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<PostsResponseDto> posts = emptyService.getPosts(page, size, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, posts.getContent()));
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
    public ResponseEntity<ApiResponse<?>> commentPost(@RequestBody CommentsDto comment) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponsCommetDto count = commentsService.addComment(comment, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, count));
    }

    // GET COMMENTS
    @GetMapping("/get-comments")
    public ResponseEntity<ApiResponse<?>> getComment(@RequestParam("postId") Long postId) {
        List<ResponsCommetDto> comments = commentsService.getComment(new IdDto(postId));
        return ResponseEntity.ok(new ApiResponse<>(true, null, comments));
    }
}
