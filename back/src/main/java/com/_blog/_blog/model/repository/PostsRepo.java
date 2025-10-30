package com._blog._blog.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.PostsEntity;

@Repository
public interface PostsRepo extends JpaRepository<PostsEntity, Long> {
    
}