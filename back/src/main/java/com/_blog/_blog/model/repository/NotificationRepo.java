package com._blog._blog.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.NotificationEntity;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationEntity, Long> {
     long countByUserIdAndReadFalse(Long userId);
     List<NotificationEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    
}
