package com._blog._blog.model.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.PostsEntity;

@Repository
public interface PostsRepo extends JpaRepository<PostsEntity, UUID> {

        // Page<PostsEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

        Page<PostsEntity> findByAuthorIdInOrderByCreatedAtDesc(List<UUID> authorIds, Pageable pageable);

        // List<PostsEntity> findByAuthorIdOrderByCreatedAtDesc(UUID authorId);
        @Query("""
                            SELECT p
                            FROM PostsEntity p
                            LEFT JOIN FETCH p.media
                            WHERE p.author.id = :authorId
                            ORDER BY p.createdAt DESC
                        """)
        List<PostsEntity> findByAuthorIdWithMedia(@Param("authorId") UUID authorId);

        List<PostsEntity> findAllByOrderByCreatedAtDesc();

        Page<PostsEntity> findByAuthorIdInAndStatusNotOrderByCreatedAtDesc(List<UUID> ids, String status,
                        Pageable pageable);

        @Query("""
                            SELECT DISTINCT p
                            FROM PostsEntity p
                            LEFT JOIN FETCH p.media
                            WHERE p.author.id IN :authorIds AND p.status <> :status
                            ORDER BY p.createdAt DESC
                        """)
        List<PostsEntity> findPostsWithMediaByAuthors(@Param("authorIds") List<UUID> authorIds,
                        @Param("status") String status,
                        Pageable pageable);

}
