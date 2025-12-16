package com._blog._blog.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.AuthEntity;

@Repository
public interface AuthRepo extends JpaRepository<AuthEntity, UUID> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByType(String type);

    Optional<AuthEntity> findByUsernameOrEmail(String username, String email);

    @Query("SELECT u FROM AuthEntity u WHERE u.id <> :currentUserId")
    List<AuthEntity> findAllExcept(@Param("currentUserId") UUID currentUserId);

    List<AuthEntity> findByUsernameContainingIgnoreCase(String username);
}
