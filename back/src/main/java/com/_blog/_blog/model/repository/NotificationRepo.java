package com._blog._blog.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.NotificationEntity;
import com._blog._blog.util.NotificationType;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationEntity, Long> {

    long countByUserIdAndReadFalse(Long userId);

    List<NotificationEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsBySenderIdAndUserIdAndType(Long senderId, Long userId, NotificationType type);

    boolean existsBySenderIdAndUserIdAndTypeAndPostId(Long senderId, Long userId, NotificationType type, Long postId);

    void deleteBySenderIdAndUserIdAndType(Long senderId, Long userId, NotificationType type);

    void deleteBySenderIdAndUserIdAndTypeAndPostId(Long senderId, Long userId, NotificationType type, Long postId);

}
