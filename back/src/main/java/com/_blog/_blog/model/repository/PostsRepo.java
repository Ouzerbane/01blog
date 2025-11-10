package com._blog._blog.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.PostsEntity;

@Repository
public interface PostsRepo extends JpaRepository<PostsEntity, Long> {

    Page<PostsEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<PostsEntity> findByAuthorIdInOrderByCreatedAtDesc(List<Long> authorIds, Pageable pageable);

    List<PostsEntity> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

}
