package com._blog._blog.dto;

import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=(?:.*[A-Za-z]){2,})(?=(?:.*\\d){2,}).*$", message = "Password must contain at least 2 letters and 2 digits")
    private String password;

    public AuthEntity toAuthEntity() {
        return AuthEntity.builder()
                .username(this.username)
                .email(this.email)
                .type("USER")
                .Action("ACTIVE")
                .password(this.password)
                .build();
    }

    public void normalizeAndValidate() {
        if (username != null) {
            if (username.length() != username.trim().length()) {
                throw new CustomException(
                        "username",
                        "It should not contain a espace at the beginning or the end.");
            }
        }

        if (email != null) {
            email = email.trim();
        }
    }

}
