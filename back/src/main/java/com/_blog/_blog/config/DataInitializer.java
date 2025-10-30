package com._blog._blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.repository.AuthRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AuthRepo authRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.type}")
    private String adminType;

    @Override
    public void run(String... args) {
        boolean adminExists = authRepo.existsByType(adminType);

        if (!adminExists) {
            AuthEntity admin = AuthEntity.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .type(adminType)
                    .build();

            authRepo.save(admin);
            System.out.println("Admin account created successfully: " + adminEmail);
        } else {
            System.out.println("Admin account already exists.");
        }
    }
}
