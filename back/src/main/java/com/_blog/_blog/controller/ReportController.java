package com._blog._blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.ReportDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.service.ReportService;

import jakarta.validation.Valid;

@RestController
// @RequestMapping("/report")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/report-user")
    public ResponseEntity<ApiResponse<?>> reportUser(@Valid @RequestBody ReportDto reportDto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reportService.reportUserService(reportDto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

    @PostMapping("/report-post")
    public ResponseEntity<ApiResponse<?>> reportPost(@Valid @RequestBody ReportDto reportDto) {
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reportService.reportPostService(reportDto, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }
}
