package com._blog._blog.model.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.LikesEntity;

@Repository
public interface LikesRepo extends JpaRepository<LikesEntity, UUID> {

    Optional<LikesEntity> findByUserIdAndPostId(UUID userId, UUID postId);

    boolean existsByUserIdAndPostId(UUID currentUserId, UUID postId);

    Long countByPostId(UUID postId);

    void deleteByUserIdAndPostId(UUID userId, UUID postId);
}
