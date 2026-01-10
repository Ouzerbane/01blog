package com._blog._blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username or Email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=(?:.*[A-Za-z]){2,})(?=(?:.*\\d){2,}).*$", message = "Password must contain at least 2 letters and 2 digits")
    private String password;
}
