package com._blog._blog.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com._blog._blog.model.entity.CommentsEntity;

public interface CommentsRepo extends JpaRepository<CommentsEntity, Long>  {
    long countByPostId(Long postId);
    
} 
