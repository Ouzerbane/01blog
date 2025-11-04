package com._blog._blog.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.LikesEntity;

@Repository
public interface LikesRepo extends JpaRepository<LikesEntity, Long> {
    
    // باش نعرف واش user دار like لبوست معيّن
    Optional<LikesEntity> findByUserIdAndPostId(Long userId, Long postId);

    // باش نحسب عدد اللايكات ديال بوست معيّن
    Long countByPostId(Long postId);

    // باش نحيد لايك ديال المستخدم
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
