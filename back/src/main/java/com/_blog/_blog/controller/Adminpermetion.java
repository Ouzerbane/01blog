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
import com._blog._blog.dto.IdDto;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.service.AdminService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })

public class Adminpermetion {
    @Autowired
    AdminService AdminService ;

    @PostMapping("/remove-user")
    public ResponseEntity<?> removeUser(@Valid @RequestBody IdDto id) {
        
        AuthEntity currentUser = (AuthEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String message = AdminService.removeUserService(id, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(true, null, message));
    }

    
}
