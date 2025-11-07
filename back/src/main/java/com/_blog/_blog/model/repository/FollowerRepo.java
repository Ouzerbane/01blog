package com._blog._blog.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._blog._blog.model.entity.FollowerEntity;

@Repository
public interface FollowerRepo extends JpaRepository<FollowerEntity, Long> {

    Optional<FollowerEntity> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<FollowerEntity> findAllByFollowerId(Long followerId);

    Long countByFollowingId(Long followingId);

    Long countByFollowerId(Long followingId);

    List<FollowerEntity> findByFollowerId(Long followerId);


    // List<FollowerEntity> findAllByFollowingId(Long followingId);

    // List<FollowerEntity> findAllByFollowerId(Long followerId);

    // void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}