package com._blog._blog.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.ErrorItem;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.PostAdminDto;
import com._blog._blog.dto.ResponsCommetDto;
import com._blog._blog.dto.UserAdminDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.entity.ReportEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.PostsRepo;
import com._blog._blog.model.repository.ReportRepo;
import com._blog._blog.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })

public class Adminpermetion {
    @Autowired
    AdminService AdminService;

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private ReportRepo reportRepoy;

    @Autowired
    private PostsRepo postRepo;

    @DeleteMapping("/remove-user")
    public ResponseEntity<?> removeUser(@Valid @RequestBody IdDto id) {

        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String isAdmine = currentUser.getType();
        if (!isAdmine.equals("ADMIN")) {
            List<ErrorItem> errors = List.of(
                    new ErrorItem("ACCESS_DENIED", "You are not allowed to access statistics"));
            return ResponseEntity.status(403).body(new ApiResponse<>(false, errors, null));
        }
        String message = AdminService.removeUserService(id, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, message));
    }

    @GetMapping("/user-type")
    public ResponseEntity<ApiResponse<?>> arIsAdmine() {

        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String isAdmine = currentUser.getType();
        if (isAdmine.equals("ADMIN")) {
            return ResponseEntity.ok(new ApiResponse<>(true, null, true));
        }
        List<ErrorItem> errors = List.of(
                new ErrorItem("ACCESS_DENIED", "You are not allowed to access statistics"));
        return ResponseEntity.status(403).body(new ApiResponse<>(false, errors, null));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> getStatistics() {

        AuthEntity currentUser = (AuthEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!"ADMIN".equals(currentUser.getType())) {
            List<ErrorItem> errors = List.of(
                    new ErrorItem("ACCESS_DENIED", "You are not allowed to access statistics"));
            return ResponseEntity.status(403).body(new ApiResponse<>(false, errors, null));
        }

        List<AuthEntity> users = authRepo.findAllExcept(currentUser.getId());
        // List<ReportEntity> reports = reportRepoy.findAll();
        // List<PostsEntity> Posts = postRepo.findAll();

        List<UserAdminDto> dtos = users.stream()
                .map(user -> new UserAdminDto(user))
                .collect(Collectors.toList());

        // List<PostAdminDto> postsDto = Posts.stream()
        // .map(post -> PostAdminDto.builder()
        // .id(post.getId())
        // .title(post.getTitle())
        // .content(post.getContent())
        // .imageUrl(post.getImageUrl())
        // .createdAt(post.getCreatedAt())
        // .countLike(post.getLikes() != null ? (long) post.getLikes().size() : 0L)
        // .countCommets(post.getComments() != null ? (long) post.getComments().size() :
        // 0L)
        // .like(false)
        // .author(PostAdminDto.AuthorDto.builder()
        // .id(post.getAuthor().getId())
        // .username(post.getAuthor().getUsername())
        // .email(post.getAuthor().getEmail())
        // .build())
        // .build())
        // .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, null, dtos));
    }

    @PutMapping("/BanUser")
    public ResponseEntity<ApiResponse<?>> getComment(@Valid @RequestBody IdDto id) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!"ADMIN".equals(currentUser.getType())) {
            List<ErrorItem> errors = List.of(
                    new ErrorItem("ACCESS_DENIED", "You are not allowed to access statistics"));
            return ResponseEntity.status(403).body(new ApiResponse<>(false, errors, null));
        }

        AuthEntity banUser = authRepo.findById(id.getId()).orElseThrow(() -> new CustomException("user","User not found"));
        banUser.setAction("BAN") ;
        banUser = authRepo.save(banUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, banUser));
    }

}
