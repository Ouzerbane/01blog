package com._blog._blog.model.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._blog._blog.model.entity.PostMediaEntity;

public interface PostMediaRepo  extends JpaRepository<PostMediaEntity, UUID>{
    
    List<PostMediaEntity> findAllByPostId(UUID postId);
}
