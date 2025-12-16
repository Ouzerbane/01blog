package com._blog._blog.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.JwtEntity;

@Repository
public interface JawtRepo extends JpaRepository<JwtEntity, UUID> {
    boolean existsByJwt(String jwt);
}
