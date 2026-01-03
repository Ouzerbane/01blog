package com._blog._blog.model.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.NotificationEntity;
import com._blog._blog.util.NotificationType;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationEntity, UUID> {

    long countByUserIdAndReadFalse(UUID userId);

    List<NotificationEntity> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    NotificationEntity findByIdAndUserId(UUID Id ,UUID userId);

    boolean existsBySenderIdAndUserIdAndType(UUID senderId, UUID userId, NotificationType type);

    boolean existsBySenderIdAndUserIdAndTypeAndPostId(UUID senderId, UUID userId, NotificationType type, UUID postId);

    void deleteBySenderIdAndUserIdAndType(UUID senderId, UUID userId, NotificationType type);

    void deleteBySenderIdAndUserIdAndTypeAndPostId(UUID senderId, UUID userId, NotificationType type, UUID postId);

}
