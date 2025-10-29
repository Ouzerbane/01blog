package com._blog._blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.repository.EmpRepo;
import com._blog._blog.util.jwt.JwtUtil;

@Service
public class EmptyService {

    @Autowired
    private EmpRepo empRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final JwtUtil jwtUtil;

    public EmptyService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login(String usernameOrEmail, String rawPassword) {

        AuthEntity user = empRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtUtil.generateToken(user.getUsername());
    }

    public AuthEntity registerservece(AuthEntity req) {
        if (empRepo.existsByUsername(req.getUsername())) {
            throw new CustomException("username", "Username already exists");
        }

        if (empRepo.existsByEmail(req.getEmail())) {
            throw new CustomException("email", "Email already exists");
        }

        req.setPassword(passwordEncoder.encode(req.getPassword()));
        return empRepo.save(req);
    }

}
