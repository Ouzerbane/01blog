package com._blog._blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.RegisterRequest;
import com._blog._blog.exception.CustomException;
import com._blog._blog.exception.ForbiddenException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.util.jwt.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private AuthRepo empRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login(String usernameOrEmail, String rawPassword) {
        AuthEntity user = empRepo.findByUsernameOrEmail(usernameOrEmail.trim().toLowerCase(), usernameOrEmail.trim().toLowerCase())
                .orElseThrow(() -> new CustomException("user", "User not found"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new CustomException("password", "Invalid password");
        }
        if (!user.getType().equals("ADMIN")) {
            if (user.getAction().equals("BAN")) {
                throw new ForbiddenException("User", "User is ban");
            }
        }
        return jwtUtil.generateToken(user.getId(), user.getUsername());
    }

    public AuthEntity registerservece(RegisterRequest req) {

        req.normalizeAndValidate();

        AuthEntity autentity = req.toAuthEntity();
        if (empRepo.existsByUsername(autentity.getUsername())) {
            throw new CustomException("username", "Username already exists");
        }

        if (empRepo.existsByEmail(autentity.getEmail())) {
            throw new CustomException("email", "Email already exists");
        }

        autentity.setPassword(passwordEncoder.encode(autentity.getPassword()));
        return empRepo.save(autentity);
    }

}
