package com._blog._blog.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com._blog._blog.model.entity.ReportEntity;

public interface  ReportRepo extends JpaRepository<ReportEntity, Long> {
    
}
