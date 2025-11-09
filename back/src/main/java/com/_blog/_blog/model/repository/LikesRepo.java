package com._blog._blog.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.LikesEntity;

@Repository
public interface LikesRepo extends JpaRepository<LikesEntity, Long> {

    Optional<LikesEntity> findByUserIdAndPostId(Long userId, Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    Long countByPostId(Long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);
}
