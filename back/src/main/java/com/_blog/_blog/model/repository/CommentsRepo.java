package com._blog._blog.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.CommentsEntity;

@Repository
public interface CommentsRepo extends JpaRepository<CommentsEntity, Long>  {
    long countByPostId(Long postId);
    List<CommentsEntity> findAllByPostIdOrderByCreatedAtAsc(Long postId);
} 
