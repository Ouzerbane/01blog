package com._blog._blog.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.NotificationDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.service.Notificationservice;

import jakarta.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class NotificationController {

    @Autowired
    private Notificationservice notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<?>> getAllNotifications() {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<NotificationDto> notifications = notificationService.getNotificationsAndMarkRead(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, null, notifications));
    }

    @GetMapping("/count-notifications")
    public ResponseEntity<ApiResponse<?>> getCountNotifications() {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long notifications = notificationService.countUnreadNotifications(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, null, notifications));
    }

    @PutMapping("/mark-notifications-read/{id}")
    public ResponseEntity<ApiResponse<?>> markNotificationsAsRead(
            @PathVariable UUID id) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        NotificationDto noti = notificationService.notificationsRead(id, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, noti));

    }
}
