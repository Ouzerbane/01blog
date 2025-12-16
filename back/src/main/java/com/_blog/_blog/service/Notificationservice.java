package com._blog._blog.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.NotificationDto;
import com._blog._blog.model.entity.NotificationEntity;
import com._blog._blog.model.repository.NotificationRepo;

@Service
public class Notificationservice {
    @Autowired
    private NotificationRepo notificationRepo;

    public long countUnreadNotifications(UUID userId) {
        return notificationRepo.countByUserIdAndReadFalse(userId);
    }

    public List<NotificationDto> getNotificationsAndMarkRead(UUID userId) {
        List<NotificationEntity> notifications = notificationRepo.findAllByUserIdOrderByCreatedAtDesc(userId);

        notifications.forEach(n -> n.setRead(true));
        notificationRepo.saveAll(notifications);

       return notifications.stream()
            .map(f -> new NotificationDto(f.getId(), f.getMessage()))
            .collect(Collectors.toList());
    }

}
