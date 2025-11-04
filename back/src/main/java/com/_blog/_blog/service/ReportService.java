package com._blog._blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.ReportDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.ReportEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.ReportRepo;

@Service
public class ReportService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private AuthRepo authRepo;

    public void reportUserService(ReportDto reportDto, AuthEntity currentUser) {
        AuthEntity targetUser = authRepo.findById(reportDto.getTargetUserId())
                .orElseThrow(() -> new CustomException("user", "User not found"));

        ReportEntity report = ReportEntity.builder()
                .reporter(currentUser)
                .targetUser(targetUser)
                .reason(reportDto.getReason())
                .build();

        reportRepo.save(report);
    }
}
