package com._blog._blog.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.FollowerEntity;

@Repository
public interface FollowerRepo extends JpaRepository<FollowerEntity, UUID> {

    Optional<FollowerEntity> findByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    Long countByFollowingId(UUID followingId);

    Long countByFollowerId(UUID followingId);

    List<FollowerEntity> findByFollowerId(UUID followerId);

    List<FollowerEntity> findAllByFollowerId(UUID followerId);

    List<FollowerEntity> findAllByFollowingId(UUID followingId);

 

    // void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
