package com._blog._blog.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com._blog._blog.model.entity.ReportEntity;

public interface ReportRepo extends JpaRepository<ReportEntity, Long> {

    @Query("SELECT r FROM ReportEntity r ORDER BY r.createdAt DESC")
    List<ReportEntity> findAllOrderByTime();

}
