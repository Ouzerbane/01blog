package com._blog._blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.ErrorItem;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.PostAdminDto;
import com._blog._blog.dto.ReportDataDto;
import com._blog._blog.dto.UserAdminDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")


public class Adminpermetion {

    @Autowired
    AdminService AdminService;


    @DeleteMapping("/remove-user")
    public ResponseEntity<?> removeUser(@Valid @RequestBody IdDto id) {

        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        List<UserAdminDto> dtos = AdminService.getUsersService(currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, dtos));

    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<?>> getPosts() {

        AuthEntity currentUser = (AuthEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        List<PostAdminDto> posts = AdminService.getPostsService(currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, posts));

    }

    @PutMapping("/BanUser")
    public ResponseEntity<ApiResponse<?>> getComment(@Valid @RequestBody IdDto id) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String message = AdminService.BanService(currentUser, id);
        return ResponseEntity.ok(new ApiResponse<>(true, null, message));
    }

    @PutMapping("/changeStatus")
    public ResponseEntity<ApiResponse<?>> chengeStatus(@Valid @RequestBody IdDto id) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String message = AdminService.ChangeStatusService(currentUser, id);
        return ResponseEntity.ok(new ApiResponse<>(true, null, message));
    }

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<?>> getReports() {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        List<ReportDataDto> reports = AdminService.getReportService(currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, reports));

    }

    @DeleteMapping("/remove-report")
    public ResponseEntity<?> removeReport(@Valid @RequestBody IdDto id) {

        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String message = AdminService.removeReportService(id, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, message));
    }

}
