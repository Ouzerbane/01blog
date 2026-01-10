package com._blog._blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.ReportDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.entity.ReportEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.PostsRepo;
import com._blog._blog.model.repository.ReportRepo;

@Service
public class ReportService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private PostsRepo postsRepo;

    public void reportUserService(ReportDto reportDto, AuthEntity currentUser) {
        reportDto.setReason(CheckReason(reportDto.getReason()));
        AuthEntity targetUser = authRepo.findById(reportDto.getTargetUserId())
                .orElseThrow(() -> new CustomException("user", "User not found"));
        ReportEntity report = ReportEntity.builder()
                .reporter(currentUser)
                .targetUser(targetUser)
                .reason(reportDto.getReason())
                .build();

        reportRepo.save(report);
    }

    public void reportPostService(ReportDto reportDto, AuthEntity currentUser) {
        reportDto.setReason(CheckReason(reportDto.getReason()));
        PostsEntity targetPost = postsRepo.findById(reportDto.getTargetPostId())
                .orElseThrow(() -> new CustomException("post", "Post not found"));

        ReportEntity report = ReportEntity.builder()
                .reporter(currentUser)
                .targetPost(targetPost)
                .reason(reportDto.getReason())
                .build();

        reportRepo.save(report);
    }

    private String CheckReason(String reason) {
        reason = reason.trim();
        if (reason.isEmpty() || reason.length() > 200 || reason.length() < 5) {
            throw new CustomException("reason", "Reason must be between 5 and 200 characters");
        }
        return reason;
    }
}
