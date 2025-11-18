package com._blog._blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.NotificationDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.service.Notificationservice;

@RestController
public class NotificationController {
      @Autowired
    private Notificationservice notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<?>> getAllNotifications() {
         AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<NotificationDto> notifications = notificationService.getNotificationsAndMarkRead(currentUser.getId());
          return ResponseEntity.ok(new ApiResponse<>(true, null, notifications));
    }
}
