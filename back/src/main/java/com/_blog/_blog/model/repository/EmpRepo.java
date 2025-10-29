package com._blog._blog.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.AuthEntity;


@Repository
public interface EmpRepo extends JpaRepository<AuthEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByType(String type);
    Optional<AuthEntity> findByUsernameOrEmail(String username, String email);
}



